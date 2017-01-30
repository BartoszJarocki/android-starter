# Android Starter

Sample Android application used as a reference for a new projects. It showcases
architecture, tools and guidelines which I use when developing for Android.

## Requirements
* Android SDK
* Latest Android version
* Latest Android SDK Tools
* Latest Android Build tools
* Android Support Repository
* JDK 1.8

## External libraries
Application:
* Retrofit - API consuming
* OkHttp - HTTP client
* GSON - JSON parsing
* Picasso - image downloading and caching
* Timber - easier logging
* ButterKnife - view binding
* Dagger 2 - dependency injection
* RxJava, RxAndroid - reactive extensions for Android
* Stetho - debugging
* LeakCannary - detecting memory leaks
* ObjectBox - data persistence 

Tests
* JUnit - unit testing framework
* Espresso - UI testing framework
* Mockito - mocking framework for unit tests in Java
* RESTMock - mocking network layer

## Building and running
From the root of the project run:
```
./gradlew installRunDebug
```

## Tests
* To run **unit** tests:
```
./gradlew test
```

Usually your presenters will execute asynchronous code. To make code synchronous for unit test purposes schedulers should be configurable See the code below:

```java
public class ThreadConfiguration {
    private Scheduler subscribeOnScheduler;
    private Scheduler observeOnScheduler;

    public ThreadConfiguration(final Scheduler subscribeOnScheduler,
        final Scheduler observeOnScheduler) {
        this.subscribeOnScheduler = subscribeOnScheduler;
        this.observeOnScheduler = observeOnScheduler;
    }

    public <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler);
    }
}
```
ThreadConfiguration should be used with compose operator from RxJava:
```java
public class ApiManager {

    private final ThreadConfiguration threadConfiguration;
    private final Api api;

    @Inject
    public ApiManager(@NonNull Api api, @NonNull ThreadConfiguration threadConfiguration) {
        this.api = api;
        this.threadConfiguration = threadConfiguration;
    }

    public Observable<Response<List<Contributor>>> contributors(String owner, String repo) {
        return api.contributors(owner, repo).compose(threadConfiguration.applySchedulers());
    }
}
```
Then in unit test it's possible to use the same thread for both subscribeOn and observeOn schedulers:
```java
    ApiManager apiManager;
    ThreadConfiguration threadConfiguration =
        new ThreadConfiguration(Schedulers.immediate(), Schedulers.immediate());

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        apiManager = new ApiManager(api, threadConfiguration);
    }
    ...
```
And this will make your code synchronous.

* To run **functional** tests:
```
./gradlew connectedAndroidTest
```

## Code analysis
* PMD (PMD is a source code analyzer. It finds common programming flaws like unused variables, empty catch blocks, unnecessary object creation, and so forth. ) [more info here][71fd51a7]
```
./gradlew pmd
```
* FindBugs (another static analysis tool) [more info here][e0b190a2]
```
./gradlew findbugs 
```
* Checkstyle (ensures that code style follows coding standard) [more info here][51d80db7]
```
./gradlew checkstyle
```

  [71fd51a7]: https://pmd.github.io/ "PMD - website"
  [e0b190a2]: http://findbugs.sourceforge.net/ "FindBugs - website"
  [51d80db7]: http://checkstyle.sourceforge.net/ "Checkstyle - website"

## Check task
To run code analysis tools and unit tests run:
```
./gradlew clean check
```

This will run **Checkstyle**->**Findbugs**->**PMD**->**Android Lint**->**Unit tests**

## Architecture

Application follows the Model-View-Presenter architecture.
See the below diagram to get more details.

![MVP](/art/mvp.png)

This application uses basic MVP setup. If you want to handle config changes (rotation etc.) you'll have to persist presenter instances. To read more about MVP I strongly recommend the following articles:
* [Mosby][5a0918ea]
* [Introduction to MVP on Android][5cf2f9e8]
* [Presentation Model and Passive View in MVP — The Android way][75fd7fde]

  [5a0918ea]: http://hannesdorfmann.com/mosby/mvp/ "MOSBY MVP"
  [5cf2f9e8]: https://github.com/konmik/konmik.github.io/wiki/Introduction-to-Model-View-Presenter-on-Android "Introduction to MVP on Android"
  [75fd7fde]: https://medium.com/@andrzejchm/presentation-model-and-passive-view-in-mvp-the-android-way-fdba56a35b1e#.s82zxg66f "Presentation Model and Passive View in MVP — The Android way"
  
## Offline
Everytime data is fetched from API it is also saved in local database (currently ObjectBox). In case of no internet connection local data will be displayed. Application uses repository pattern to achieve this behavior.

```java
public class AppRepository implements Repository {

    private final LocalRepository localRepository;
    private final RemoteRepository remoteRepository;

    @Inject
    public AppRepository(final LocalRepository localRepository,
        final RemoteRepository remoteRepository) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    @Override
    public Observable<List<Contributor>> contributors(final String owner, final String repo) {
        return Observable.concat(remoteRepository.contributors(owner, repo),
            localRepository.contributors(owner, repo))
            .first(contributorsResponse -> !contributorsResponse.isEmpty());
    }
}
```

```java
public class RemoteRepository implements Repository {

    private final LocalRepository localRepository;
    private final ThreadConfiguration threadConfiguration;
    private final GithubApi githubApi;
    private final ContributorsJsonsToContributorListMapper
        contributorsJsonsToContributorListMapper = new ContributorsJsonsToContributorListMapper();

    @Inject
    public RemoteRepository(final LocalRepository localRepository, @NonNull GithubApi githubApi,
        @NonNull ThreadConfiguration threadConfiguration) {
        this.localRepository = localRepository;
        this.githubApi = githubApi;
        this.threadConfiguration = threadConfiguration;
    }

    public Observable<List<Contributor>> contributors(String owner, String repo) {
        return githubApi.contributors(owner, repo)
            .flatMap(response -> {
                if (response.isSuccessful()) {
                    return Observable.just(response.body());
                } else {
                    return Observable.error(new RuntimeException());
                }
            })
            .doOnNext(localRepository::put)
            .flatMap(contributors -> Observable.just(
                contributorsJsonsToContributorListMapper.map(contributors)))
            .onErrorResumeNext(throwable -> {
                if (throwable instanceof IOException) { // network errors
                    return Observable.empty(); // show local data!
                }

                return Observable.error(throwable);
            })
            .compose(threadConfiguration.applySchedulers());
    }
}
```

```java
public class LocalRepository implements Repository {
    private final Box<ContributorEntity> localContributorBox;
    private final ThreadConfiguration threadConfiguration;
    private final ContributorsJsonsToContributorEntitiesMapper
        contributorsJsonsToContributorEntitiesMapper =
        new ContributorsJsonsToContributorEntitiesMapper();
    private final ContributorsEntitiesToContributorsMapper entitiesToContributorsMapper =
        new ContributorsEntitiesToContributorsMapper();

    public LocalRepository(final Box<ContributorEntity> localContributorBox,
        final ThreadConfiguration threadConfiguration) {
        this.localContributorBox = localContributorBox;
        this.threadConfiguration = threadConfiguration;
    }

    @Override
    public Observable<List<Contributor>> contributors(final String owner, final String repo) {
        localContributorBox.removeAll();

        return Observable.defer(() -> Observable.just(localContributorBox.getAll()))
            .flatMap(contributorEntities -> Observable.just(
                entitiesToContributorsMapper.map(contributorEntities)))
            .compose(threadConfiguration.applySchedulers());
    }

    public void put(final List<ContributorJson> contributorJsons) {
        localContributorBox.put(contributorsJsonsToContributorEntitiesMapper.map(contributorJsons));
    }
}
```


## Project setup
If you want to start a new project based on this boilerplate do the following steps:
* Download this repository as a zip
* Change the package name 
  * Rename packages in ```main```, ```androidTest``` and ```test``` 
  * Rename applicationId in ```build.gradle```
  * Rename package name in ```src/main/AndroidManifest.xml``` and ```src/androidTest/AndroidManifest.xml```
* Init a new git repository
* Make sure you want all the dependencies included in this boilerplate

## License
```
Copyright 2016 Bartosz Jarocki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
  ```

