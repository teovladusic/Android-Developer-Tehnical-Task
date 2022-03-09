# Android-Developer-Tehnical-Task
Task provided by Async Labs company

## Task
Create an application that will serve a feed with posts that are based around videos.

## Programming Language
Kotlin

## Libraries (tools) used:
Dagger Hilt, Kotlin Coroutines, LiveData, Flow, Retrofit2, DataStore Preferences, CameraX, EasyPermissions, Glide, Navigation Component, Truth, Turbine, Mockito, Espresso

## Architecture: MVVM (Model-View-ViewModel)
![mvvm](https://user-images.githubusercontent.com/71450900/157429074-4f35df67-8d4c-441c-a6bf-8bfd9fb60b89.jpg)


Views should only be responsible for interaction with users and displaying UI.


ViewModel is the “brain” of the application and its purpose is to deal with business logic. In this application I also addedUse Cases for even better decoupling and reusability of the application. Technically it is not wrong to leave Use Cases out, but I think it’s a good practice to write them as you can inject them everywhere where you might need that Use Case which also results in not repeating yourself.


Data repositories are representing the data layer of the application. They should be responsible only for communicating with Use Cases (or ViewModel) and provide them with the requested data. In this application I used api as the source of data with Retrofit2 which requests an additional service defining all the routes and http methods. Data layer in this application has its own models (dtos) which are representations of data received from api. In this case the repository also has the responsibility to map those dtos into the domain objects.

## Design
Design of the application is inspired from these resources:
Login and SignUp screens - https://dribbble.com/shots/14605167/attachments/6297430?mode=media
Profile screen - https://dribbble.com/shots/6310443-Movie-App/attachments/6310443-Movie-App?mode=media

## Data Source:
https://technicaltaskapi.docs.apiary.io/#
