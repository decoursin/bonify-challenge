#### Challenge:

Given a user with a bank account, this user gets updates through a synchronisation web hook. The data that is received through the web hook should be attached to the account that is identified in the request. 

Whenever the spending exceeds 500 Euro in a 10 day time window, the user should be notified (by calling a send notification mock)

Please create a basic system and a test given the attached files as input for the account transactions. There should be an endpoint where the data (with an additional account-identifier) arrive and will be stored into a database. Assume the input may have duplicate entries, which might need to be filtered out. 

The system must be written in Java, but feel free to use the frameworks that best suit you.

Expected Deliverables:
Unit-Test calling the web hook and testing that the data is in the database without duplicates
Controller for Web hook 
All other classes to reflect your design and create a runnable application.

---------------------------------------------------------------------------
>> Given a user with a bank account, this user gets updates through a synchronisation web hook. The data that is received through the web hook should be attached to the account that is identified in the request. 

> Build a REST API that accepts updates to a user's bank account.

While you are free to make any assumptions, this seems to be the most viable assumption, so yes.

>> Whenever the spending exceeds 500 Euro in a 10 day time window, the user should be notified (by calling a send notification mock)

> This is pretty clear, thanks. Except, a moving 10 day time window, or just a 10 day time window?

It should be a moving ten day time window. This makes it a little more tricky.


>> There should be an endpoint where the data (with an additional account-identifier) arrive and will be stored into a database.

> Is this different or the same from what you said in the first paragraph?

You are right. This is redundant.

>> with an additional account-identifier

> 'additional' what do you mean? Are you referring to the Partner_Account field?
yes.

> There is four CSV files

Why is it split into four files? There doesn't seem to be a strict pattern, except random date cutoffs
