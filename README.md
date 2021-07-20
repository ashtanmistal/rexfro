# Find and Replace Bulk Operator

## An application to perform multiple find-and-replace tasks simultaneously

### What will this application do?

The basic find and replace operations in common text editors have largely remained the same since their inception. You have the option to find an expression in a document, and replace that expression (or all instances of that expression) with something else. However, the event arises where one has to perform multiples of these operations, many times over. For example, converting a *WolframAlpha* output to *LaTeX* formatting requires numerous "find and replace all" instances, including but not limited to the following:

- Replacing all "|" characters to "&"
- replacing all weird Unicode characters to *LaTeX* commands, where each special Unicode character is a separate find and replace operation

Hence, this application allows the user to do just that: perform multiples of these operations, many times over, and in multiple documents. This would be done by storing a .csv file (or, more likely, a .tsv) of all expressions to find, what to replace said expressions with, whether or not the specific operation is case sensitive, and a Boolean of automatically replacing all instances of an expression or to prompt the user for each instance. The application allows one to perform a myriad of more complex actions rather than a simple find and replace. The application will also allow the user to create a new line of commands to perform from scratch, and the option to store it for later as a .csv or .tsv. For the most versatile method of this, the application utilizes *regular expressions*, allowing the following additional functionality along with so much more:

- Adding " \\\\" at the end of each line of outputted WolframAlpha text
- Replacing all instances of " x/y " with "\frac{x}{y}", for any values of x and y 
- replacing all instances of " .tsv" with ".csv or .tsv" **unless** " .csv or " is already to the left of " .tsv"

Hence, this allows the user to do things such as replacing entire regular mathematical expressions in a document with LaTeX commands through the import of a single .csv or .tsv file and a click of a button, turning "x = 1/28 i (sqrt(447) + i)" to "x = \frac{1}{28} i \left( \sqrt{447} + i \right)". Of course, the functionality to directly translate from plain text to LaTeX will not be native to the application without the development of a .csv for this specific purpose, but the application enables this functionality in an unprecedented manner. 

The functionality goes far beyond the translation of WolframAlpha outputs to LaTeX, however. If you have your class notes, for example, spread out over multiple documents, the application makes it far easier to fix all instances of an incorrectly spelled mathematician's name over all of your notes, by applying the same .csv or .tsv defined procedure to all of the documents, instead of opening up the documents individually, opening the find and replace feature, typing the incorrectly spelled name, and typing the correct solution, and hitting the "replace all" button. 

### Who will use it?

The intention is to make this application usable for anyone working with text documents. (**note**: *Currently only plain text document formats such as .txt, .md, and .tex are intended to be supported, as there is encoding with .word and other formats and it is unknown if the application's functionality will lead to the breaking of important encoding if it treats the document as plain text.)*. Anyone wanting to perform multiple operations on a specific piece of text would find use in the application, and it provides a central hub for different operations to be utilized. Bots on various subreddits, for example, utilize some mass text operations in order to do things like "emojify" a user's comment, turn a comment into poorly translated Shakespearean English, or "owo-ify" a user's comment. Although these examples sound silly, this application would allow easy implementation of new operations such as those mentioned to give the user more control over their documents. 

### Why is this project of interest to you?

Because I'm fed up with always having to turn the outout of a WolframAlpha query to proper LaTeX commands, and I often find myself performing multiple find and replace operations either by hand or through find and replace tools, and I want to save time when writing or creating documents. (I know I can just use Symbolab instead of WolframAlpha as it outputs LaTeX, but I really like WolframAlpha and I am stubborn and lazy and so I refuse to switch). I would love the ability to just import a .csv file (or make a new one within the application), and have all of my text operations done for me. Plus it's not an idea I've heard of before, so it'd be pretty neat to make something new. 
