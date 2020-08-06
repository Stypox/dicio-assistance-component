# Assistance component for Dicio assistant
This tool provides an interface to create **multiplatform assistance components**. An assistant component is something that tries to provide an **answer to a request** from the user. It provides a fitness score based on user input (i.e. is it a query this component is able to answer?) and provides the application using this library all of the needed data to respond to the user. A full assistance component is made up of **multiple separate components**, which process the data in a sort of *assembly line* to produce an output displayable to the user.

This repository is part of the Dicio project. Also check out [`dicio-android`](https://github.com/Stypox/dicio-android) and [`dicio-sentences-compiler`](https://github.com/Stypox/dicio-sentences-compiler/). Open to contributions :-D

## Input recognizer
An `InputRecognizer` is the first step in the *assembly line*, and it **processes input** from the user to determine if it **matches a given set of** (*custom*) **rules**, calculating a fitness score. The score is a value ranging from `0.0` to `1.0`, where 0 means "it does not match at all" and 1 means "it matches perfectly". Values between `0.85` and `1.0` usually describe a good match, while values below `0.7` are considered a bad match. When an input recognizer is evaluated as the most suitable one, it has to generate an object of type `ResultType` containing data extracted from the input, to be used in the next steps.

Every input recognizer has a specificity (`low`, `medium` or `high`) that represents **how specific the set of rules is**. E.g. one that matches only queries about phone calls is very specific, while one that matches every question about famous people has a lower specificity. The specificity is needed to prevent conflicts between two components that both match with an high score: the most specific is preferred.

## Intermediate processor
An `IntermediateProcessor` processes the data obtained from the previous step in the *assembly line* to produce a result to be passed to the next step. It is made for intermediate calculations, to connect to the internet and extract things, etc. Even though everything could be done in an `InputRecognizer`, it is better to keep things separate, so that `InputRecognizer`'s only purpose is to collect information from the user input.

## Standard input recognizer
The `StandardRecognizer` class implements `InputRecognizer` by providing a simple way to calculate the fitness score based on a list of sentences to match. E.g. `what is the weather` matches: itself with maximum score; `what is the weather like` with high score; `what is he doing` with low score; `how are you` with minimum score.

The `ResultType` of a `StandardRecognizer` is `Sentence`, which provides access to many pieces of information, namely the **sentence id**, useful to answer the question with the most appropriate language, and the capturing groups. The standard recognizer supports up to two **capturing groups**, that is variable-length lists of any word to be captured in at a certain position in a sentence. E.g. `how are you <CAPTURING_GROUP>` matches `how are you Tom` with maximum score and `Tom` is captured in the first (and only, in this case) capturing group.

A `StandardRecognizer` supports three types of words: diacritics sensitive ones, diacritics insensitive ones and capturing groups. The matching algorithm runs in `O(N*M)`, where `N` is the number of all words being matched in any sentence and `M` is the number of input words. In order to run such a Dynamic Programming top-down search efficiently, the topologically-sorted graph of the words needs to be precomputed: each word is constructed with a list of possible next words. For scoring purposes the minimum number of steps (i.e. words to pass through) to get to the end of the sentence is also provided statically.

Use [dicio-sentences-compiler](https://github.com/Stypox/dicio-sentences-compiler) to generate the sentences for an input recognizer in a simple and fast way. All of the precomputation needed by the `StandardRecognizer` is done automatically. It enables to pack sentences toghether, e.g. `hello|hi how (are you doing?)|(is it going)` matches all of `hello how are you`, `hello how are you doing`, `hello how is it going`, `hi how are you`, `hi how are you doing`, `hi how is it going`. See? Six sentences have been packed into one!
