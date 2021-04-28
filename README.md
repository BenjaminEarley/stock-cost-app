# Stock Cost

Mobile developer assignment by Benjamin Earley

## Overview

The app is made up of 3 pages:

- Product List: On the list page, you can scroll through saved products. Three default products are shipped in a DB with the app to start you off. You can swipe away to delete and press an add action button near the bottom right to open the add product page. 

- Add Product: The add product page takes a product security ID as input and, if valid, downloads the product to save locally. Closes when complete.

- View Product: When opening a product from the list page, the app checks if the locally saved product is less than an hour old else updates the cache. It then shows the product's name, id, symbol, current price, closing price, and the percent difference. A websocket connection is attempted to retrieve the live price stream while the page is open. You can pull to refresh to force load the latest product data. 

## Architecture

The codebase is setup with an MVVM like architecture with a focus on unidirectional data flow. The app is broken up into 3 distinct layers:

- Fragment/UI: Responsible for handling the Android view lifecycle and drawing the UI.

- ViewModel: Responsible for maintaining the View state of the fragment and handling user actions.

- Repository/Model: Responsible for maintaining the Domain state for the app. In this example, it is responsible for providing the Products. The repository has required dependencies to access both the network and local database.

## Testing

Two unit tests are provided to demonstrate testing the Model layer business logic using simple dependency injection to inject fake data. Generative testing has been an area I've been interested in recently, and provided a utility to lazily generate fake products. In the second test, you can see that 1,000 random products are generated for both the network and db to test that the Model chooses the correct one based on the given cache rule. 

## Dependencies Overview

- Kotlin + Coroutines: I have in the last year migrated entirely off of RX onto coroutines and have been enjoying it. The biggest win on coroutines over RX is the topic of structured concurrency. Here is an [article](https://vorpus.org/blog/notes-on-structured-concurrency-or-go-statement-considered-harmful/) on that.

- AndroidX: The jetpack effort is making Android development much more enjoyable and is a must now.

- Hilt: I have usually handled DI myself with just using constructors, but the Hilt abstraction on Dagger2 makes integrating dagger into Android way simpler. With much less complexity overhead, I've found myself using it now.

- Material: Google's implementation of material design on Android has matured greatly over the years. There is a learning curve on how they have their styling structured but I've found it to make UI much more consistent out of the box compared to the basic built in components. 

- Λrrow: I am a big fan of [Railway Oriented Programming](https://fsharpforfunandprofit.com/rop/) and their Either implementation is simple but feature rich. Many other useful utilities in here.

- Ktor: First time using this in an Android only project. I chose it to hopefully help simplify my websocket implementation which was also a first for me. I don't think it helped really, the project assignment also mentioned Scarlet which looks promising and will need to check that out in the future. 

