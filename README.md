# Find and Replace Plus

## An application to perform multiple find-and-replace tasks simultaneously

### What will this application do?

The basic find and replace operations in common text editors have largely remained the same since their inception. You have the option to find an expression in a document, and replace that expression (or all instances of that expression) with something else. However, the event arises where one has to perform multiples of these operations, many times over. For example, converting a *WolframAlpha* output to *LaTeX* formatting requires numerous "find and replace all" instances:

- Replacing all "|" characters to "&"
- replacing all weird Unicode characters to *LaTeX* commands

Hence, this application allows the user to do just that: Store a .csv file (or, more likely, a .tsv) of all expressions to find, what to replace said expressions with, and whether or not to prompt the user for each individual instance of the expression or to automatically replace all instances at once. This functionality allows one to perform a myriad of more complex actions rather than a simple find and replace, for the most versataile method of this utilizes *regular expressions*, allowing the additional functionality and so much more:

- Adding " \\" at the end of each line of outputted WolframAlpha text
- Replacing all instances of " x/y " with "\frac{x}{y}", for any values of x and y 

Hence, this allowes the user to do things such as replacing entire regular mathematical expressions with LaTeX commands through the import of a single .tsv file and a click of a button, turning "x = 1/28 i (sqrt(447) + i)" to "x = \frac{1}{28} i \left( \sqrt{447} + i \right)". of course, the functionality to directly translate from plain text to LaTeX will not be native to the application, however the application enables this functionality in a (to my knowledge) unprecedented manner. 

The functionality goes far beyond the translation of WolframAlpha outputs to LaTeX, however. If you have your class notes, for example, spread out over multiple documents, the application makes it far easier to fix all instances of an incorrectly spelled mathematician's name over all of your documents, by applying the same .tsv defined procedure to all of the documents, instead of opening up the documents individually, opening the find and replace feature, typing the incorrectly spelled name, and typing the correct solution, and hitting the "replace all" button. 

### Who will use it?

