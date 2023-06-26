<h1 align="center">Lazy Reader</h1>

<p align="center">
Lazy Reader is a book reader app designed to enhance your reading experience. Our app combines convenience, customization, and cutting-edge technology to provide an immersive and effortless reading experience. We understand that reading is a cherished pastime for many, but with the fast-paced nature of modern life, finding time to sit down and read can be challenging. That's where Lazy Reader comes in. Lazy Reader allows users to search a vast range of books, from classic literature to contemporary bestsellers, all in one convenient app. User's can search for a book, save that in their reading list, write some reviews and give that book a rating. One more thing which makes this app special is user can now download a book with a click using Google. Whether you prefer novels, non-fiction, or self-help books, Lazy Reader has something for everyone.
</p>

<p align="center">
<img src="https://github.com/bishalbera/Jet-Lazy-Reader-/assets/123734227/2ee077b1-d7e6-4568-862f-6914ebb5e8ec" alt="" />
</p>


### Tech stack & libraries:

* [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
    
* Jetpack Compose :- Modern UI toolkit for Android development.
    
    * Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
        
    * ViewModel: Manages UI-related data holder and lifecycle awareness. Allows data to survive configuration changes such as screen rotations.
        
    * [Hilt](https://dagger.dev/hilt/): for dependency injection.
        
* Architecture
    
    * MVVM Architecture
        
* [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs and paging network data.
    
* [Material-Components](https://m3.material.io/develop/android/jetpack-compose): Material design components for building ripple animation, and CardView.
    
* [Coil](https://github.com/coil-kt/coil): Loading images from the network.
    
* [Firebase](https://firebase.google.com/): Works as backend.
    
* [Lottie](https://lottiefiles.com/): Lottie is a file format for vector graphics animation.
    
* [Paging3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview): Gives a smooth pagination experience.
    

### **Screens & Features:**

Lazy Reader provides a seamless user experience through its intuitive and user-friendly interface. Let's explore the key screens and features of the app:

1. **Login Screen**: The login screen is the entry point for users to access the Lazy Reader app. Users can either create a new account or sign in with their existing credentials.
    
2. **Search Screen:** Once logged in, users can search for their required books in the search screen. One can also search via provided categories.
    
3. **Home Screen:** In the home screen users can find their saved books. Users can also keep track of their currently reading books.
    
4. **Details Screen:** When a user tap on the book from the search results, they are taken to the book details screen. There users can see the book's cover pic, authors, average rating, published date & description. Not only that on clicking on search books users will be taken to the browser's page where they can download that book from the search result.
    
5. **Update and Profile Screen:** On tapping any of the book cards in the home screen users can update the status of the book from the reading list to the current reading list, users can write reviews and give a rating. In the currently reading list if the user taps finish reading and update it then it will be added to the profile screen, where users can see their read books.
