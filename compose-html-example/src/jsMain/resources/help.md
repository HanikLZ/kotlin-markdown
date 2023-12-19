# kotlin-markdown
> A Markdown parser written in Kotlin.

Features
---
---
- Easy to extend
  - Easy to add, remove or replace line block parser.
 
  - Standards-compliant.

  - Render implemented by Compose html.

- Pure Kotlin
  - Multiple platform library.
   
Installation
---
---
Gradle dependencies:
```kotlin
implementation("mdvsc.doc:markdown:1.0.0")
```
Built-in supported
---
---
- Headings
  - > `#` before text or line 
    > ```
    > # Level1
    > 
    > ## Level2
    > 
    > ### Level3
    > 
    > #### Level4
    > 
    > ##### Level5
    > 
    > ###### Level6
    > ```
  - > Any number of == characters for heading level 1 or -- characters for heading level 2 below text or line.
    > ```
    > Level1
    > ===
    > 
    > Level2
    > ---
    > ```
  - > Headings id
    > ```
    > # Headings {# id}
    > 
    > Headings {# id}
    > ---
    > ```
    > Headings [#](#Headings) {# Headings}
    > ---
- Link
  > ```
  > [Link](url)
  > 
  > [Link](url "tooltip")
  > 
  > <url>
  > ```
  > Automatic URL Linking
  > 
  > https://github.com
- Image
  > ```
  > ![Tux, the Linux mascot](/assets/images/tux.png)
  > ```
- Table
  > ```
  > | Syntax      | Description |
  > | ----------- | ----------- |
  > | Header      | Title       |
  > ```
  > Align
  > ```
  > | Left | Right | Middle |
  > | :----| ----: | :----: |
  > | C1   | C2    | C3     |
  > ```
- List
  > Unordered list
  > ```
  > - line1
  > * line2
  > + line3
  > ```
  > Ordered list
  > ```
  > 1. line1
  > 2. line2
  > ```
  > Task list
  > ```
  > [X] Checked
  > [ ] Unchecked
  > 
  > 1. [X] Checked
  > 2. [ ] Unchecked
  > 
  > - [X] Checked
  > - [ ] Unchecked
  > ```
- Definition list
  > ```
  > First Term
  > : This is the definition of the first term.
  > 
  > Second Term
  > : This is one definition of the second term.
  > : This is another definition of the second term.
  > ```
- Code
  - > Indent every line of the block by at least four spaces or one tab
  - > Three tildes (```) on the lines before and after the code block 
- Quotes
  > ```
  > Use > in front of a paragraph, and 
  > ```
  >> ```
  >> >> or more in front of the paragraph to create nest quotes.
  >> ```
- Footnotes
  > ```
  > Here's a simple footnote,[^1] and here's a longer one.[^bignote]
  >
  > [^1]: This is the first footnote.
  > [^bignote]: Here's one with multiple paragraphs and code.
  > ```
- Line break
  > End a line with two or more spaces, and then type return create `<br>`.
- Paragraphs
  > Use a blank line to separate one or more lines of text create paragraphs. 
- Horizontal Rules
  > ```
  > ***
  > ---
  > _________________
  > ```
- Emphasis
    - > `**Bold**` 
      > **Bold**
      > 
    - > `*Italic*`
      > *Italic*
      >
    - > `***BoldItalic***` 
      > ***BoldItalic***
      > 
    - > `~~Strikethrough~~` 
      > ~~Strikethrough~~
      > 
    - > `==Highlight==` 
      > ==Highlight==
      > 
    - > `^Superscript^` 
      > ^Superscript^
      > 
    - > `~Subscript~` 
      > ~Subscript~
      > 
    - > ```
      > `Code` 
      > ```

Built-in renderer
---
---
### Compose html
- Markdown Component
  ```kotlin
    @Composable
  
    fun Component() {
        Markdown(markdownContent)
    }
  ```
