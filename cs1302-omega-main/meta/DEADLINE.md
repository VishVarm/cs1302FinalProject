# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice looking HTML.

## Part 1: App Description

> Please provide a firendly description of your app, including the
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**


    The app that I have created is a currency converter app that allows users
    to input an amount of currency and then choose the type of currency to convert
    from and to. It then prints the amount of currency after conversion. The user
    can also specify whether to use their ip address to automatically select their home
    currency to convert from/to. The GUI consists of a top hbox containing a textfield for
    the user to input starting currency amount, two comboboxes for the user to select which
    currencies to convert to and from, and a convert button to start the conversion. The
    comboboxes also have an option to automatically select the users local currency based
    on their ip address location. Underneath,there is a label where the converted value
    gets printed. Under that, there is a label warning users to not press the button and call
    the API too rapidly. At the bottom, there is a testing area where a tester could input
    any location ip address as the starting currency using the rest of the controls at the top
    for the other options, and a test convert button to perform the test conversion.
    The two apis I used in this project are the Exchangerate.host API to retrieve rates
    between currencies and the Abstract API to retrieve location and currency code from
    the user's ip address. Once either button is pressed a call is made to the Abstract API
    with either the user's ip address or the inputted one. This call retrieves the ip address
    currency code, which is then used in the call to the Exchangerate.host API as one of the
    currencies to be converted from/to. These APIs are meaningfully connected as the
    Exchangerate.host API requires two currency codes to convert from/to, and the Abstract
    API provides one of these currency codes.x

    GitHub 'https' URL: https://github.com/VishUGA/cs1302-omega


## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

    In this project, I furthered my knowledge on APIs as, before this project, I was very
    uncomfortable and unfamiliar working with APIs, but this project has allowed me to
    work very close with APIs and find new ways to use them, such as connecting them
    meaningfully. I also learnt about how to use GitHub, a very helpful tool when making
    any coding project and a website I am sure to use in the future.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

    If I could redo this project, I would probably find a way to include more information
    in my app as my current app that prints only the converted value is a tad bit limited.
    I could have also printed out a graph showing how the conversion rate between two
    currencies has changed over time and allow the user to make the decisionif this is a
    good time to convert. I could have also spent more time on the GUI as my current GUI
    is very boring and focused on functionality. I could have spent more time on it and
    made it more aesthetically pleasing.
