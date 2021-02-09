# Bazaar Android Architecture
Bazaar's Android Architecture


Repository Model:
[RepoManager] -> [Repo Layers] -> [Service Layers] -> [Backend APIs]
[RepoManager] -> [Repo Layers] -> [Service Layers] -> [DataBase]
[RepoManager] -> [Repo Layers] -> [SharePreference]


Multiple Backend
Hide Repo, Service, DB, Backend layers from View and ViewModel. View has no knowledge whether we are storing data in Shared preference or calling api
Documentation Easy

All layers are interfaces, so multiple implementations
Mock implementation can easily be made for integration testing

Why Manager:
View Model will doesn't have to know about repo layers and further.
Will help in converting it into SDK
If we remove any repo layer, No effects on ViewModel

Why Repo Layer:
Repo layer can integrate multiple services in it.
Remapping of data is easy

Why Service Layer: 
To hide backend layer from Repo Layers
Single responsibility for each service
No data manipulation and mapping