# Bazaar Android Architecture Template
Bazaar's Android Architecture

### What is MVI
Like MVC, MVP or MVVM, MVI is an architectural design pattern that helps us better organize our code to create robust and maintainable applications. It is in the same family as Flux or Redux and was first introduced by André Medeiros. This acronym is formed by the contraction of the words Model, View and Intent.
<p align="center">
  <img src="https://github.com/bazaartechnologies-oss/android-mvi-architecture-kotlin/blob/master/github/mvi_image.png">
</p>

### Concept reference resources 
- [MVI Architecture - Android Tutorial for Beginners - Step by Step Guide](https://blog.mindorks.com/mvi-architecture-android-tutorial-for-beginners-step-by-step-guide)
- [Getting started with MVI Architecture on Android](https://proandroiddev.com/getting-started-with-mvi-architecture-on-android-b2c280b7023)


#### The app has following purpose:
This application is a template repository for Android Application in "Bazaar Technologies". So whenever we want to create a new application from scratch we can create repository using this template.<br/>
We need to change package name<br/>
We need to add tokens, base url and other static content if needed<br/>
We need to replace google-services.json file with new Firebase project (If you are not using firebase, then remove its code)<br/>


### Repository Model:
[RepoManager] -> [Repo Layers] -> [Service Layers] -> [Backend APIs] <br/>
[RepoManager] -> [Repo Layers] -> [Service Layers] -> [DataBase]<br/>
[RepoManager] -> [Repo Layers] -> [SharePreference] <br/>

<p align="center">
  <img src="https://github.com/bazaartechnologies-oss/android-mvi-architecture-kotlin/blob/master/github/RepositoryPattern.png">
</p>

Multiple Backend<br/>
Hide Repo, Service, DB, Backend layers from View and ViewModel. View has no knowledge whether we are storing data in Shared preference or calling api
Documentation Easy<br/><br/>

All layers are interfaces, so multiple implementations<br/>
Mock implementation can easily be made for integration testing<br/>

Why Manager:<br/>
View Model will doesn't have to know about repo layers and further.<br/>
Will help in converting it into SDK<br/>
If we remove any repo layer, No effects on ViewModel<br/>

Why Repo Layer:<br/>
Repo layer can integrate multiple services in it.<br/>
Remapping of data is easy<br/><br/>

Why Service Layer: <br/>
To hide backend layer from Repo Layers<br/>
Single responsibility for each service<br/>
No data manipulation and mapping<br/><br/>


## Objectives

We set off with the objective of creating a minimalistic template, simple yet with all the 
capabilities you need.

### Simple 
The library introduces few - if any - new concepts outside MVI, 
[Kotlin coroutines](https://kotlinlang.org/docs/reference/coroutines/basics.html), and 
[Kotlin flows](https://kotlinlang.org/docs/reference/coroutines/flow.html). 

If you are familiar with those concepts, you can start using the library without a problem; if you are new to some of these
concepts, you will be able to apply those concepts outside this library too.

### Coroutines and flows as powerful abstractions 

We believe that coroutines and flows are extremely powerful concepts that can be applied to the MVI architecture. 
They enable us to build a very powerful API with a small and simple surface.

Here are some advantages that they bring:

* Coroutines make asynchronous calls very simple to write and easy to reason about;

* Flows are a great abstraction to represent user events (clicks) and updates from background work;

* Coroutine scopes make handling the lifecycle of requests very simple

## Inspiration

This library got a lot of inspiration from other libraries. We would like to thank:

* [MVFlow](https://github.com/pedroql/mvflow)
* [MVIBeginnersMindOrks](https://github.com/MindorksOpenSource/MVI-Architecture-Android-Beginners)

## If this project helps you in anyway, show your love :heart: by putting a :star: on this project :v: ........ ALSO FEEL FREE TO FORK AND CONTRIBUTE!!
