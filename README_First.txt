uniCenta oPOS v3.50
Date: 8 October 2013 @ 00:00


Known Issues
1  	uniCenta oPOS requires a correctly installed Java runtime(JRE) version 1.7 
2	uniCenta oPOS v3.50 will not run on Macs from OSX 10.6 (Apple's Java limitation) - Requires latest OSX and correct Java JRE. Please see Apple's website
3 	John L changes - Inclusion of John L's enhancements and Bug fixes - see separate documents in John L version.zip file.
4	This is the official uniCenta version and any changes you have made to previous versions using 3rd Party scripts will be over-written

Jack - uniCenta Fixes  Enhancements
1	Bug: 	No resource bundle java.util.PropertyResourceBundle,key label.margin
	Fix: 	Created pos_messages.properties.label.margin=Margin

2	Bug: 	No resource bundle java.util.PropertyResourceBundle,key label.grossprofit
	Fix: 	Created pos_messages.properties.label.grossprofit=Gross Profit

3	Bug: 	Payment Gateway JPanel forms not showing on action/change events as all Payments class files corrupted
	Fix: 	Recreated folder contents

4	Bug: 	Payment Admin People,Resources and JRoles Panel files corrupted
	Fix: 	Recreated JPanel forms

5	Bug: 	All Payment Gateway combox Events not displaying params panel
	Fix: 	Resolve underlying ActionPerformed method

6	Bug:	JTicketsBagRestaurantMap - Statement not closing due to stray ";"
	Fix:	Remove stray ";"

7	Bug:	JPanelTable - List CellRenderer statement not closing as no braces {}
	Fix:	Added braces {}

8	Bug:	Payments Notes field could not receive input from virtual keypad
	Fix:	Replaced jTextField component with jEditorString component

9	Bug:	SQL Table Create/Update scripts fails still updated APP version causing subsequent retry failures
	Fix:	Move APP version update SQL command to end of scripts

10	Bug:	Authorize.net Payment Gateway transaction failure caused by device setting
	Fix:	Change to device setting from '1' to '5' (PC device)

11	Bug:	PostgreSQL pickup_number table SQL script fail when setting SERIAL counter
	Fix:	Replace pickup_number table SQL script with PostgreSQL advisory for auto_numbering

12	Enhancement: Added ProductEditor.Button - Click Me! Button display HTML tags properties
        see http://docs.oracle.com/javase/tutorial/uiswing/components/html.html
	example: <html><color=cyan><font size=+1><b>1AA1<br>1111111</font>

13	Enhancement: Removed John L's AppTitle extensions and replaced with uniCenta version numbering

14	Enhancement: All Tahoma font use changed to Arial for X-Platform compatability

15	Enhancement: John L's work - Config panels componentS spacing & font sizings adjusted to allow for touch input ease-of-use
	
16	Enhancement: JPanelConfigDatabase:
		1. Move Button to max 600 position
		2. Extended textInputs to 480 to display long path names

17	Enhancement: Sales screen transaction line font size enlarged to Arial 14 + increased line-height

18	Enhancement: JPanelCloseMoney form Print and Close Cash buttons anchored to Bottom Right of form (all screen sizes)

19	Enhancement: Prevent NULL value Sales as Layaway. Provide Alert Message. Simple and Standard Tickets only

20	Enhancement: Main JRootApp form User Login button: Height increased and icon + text now on separate lines to accommodate long User names

21	Enhancement: Service Charge scripts added

22	Enhancement: Support for Casio PD1 Weighing Scale added

23	Enhancement: Added Warning to Configuration>System Options>Start Up Screen Text + handler

24	Enhancement: Increased Configuration>Ticket Setup>Receipt Setup Spinners

25	Enhancement: Set Configuration>Ticket Setup>Receipt Setup Spinners Minimums

26	Enhancement: Added Configuration>Database Setup>Pre-Configured Database Settings combox box

27	Enhancement: Added Configuration>Database Setup>Test Connection button

28	Enhancement: Taxes management panels moved to Maintenance Menu

29	Enhancement: Add Print On/Off icon to jPaymentSelect form

30	Enhancement: Add Phone + Email to JCustomerFinder

31	Enhancement: Flipped OK And Cancel buttons on JCustomerFinder and jProductFinder and jTicketsFinder

32	Enhancement: Added uniCenta startup Splash screen

33	Enhancement: Added CustomerView Transactions Tab

34	Enahncement: Added Customer Photo to CustomerView>Photo tab

35	Enhancement: Set Image component MaxDimensions to Null in People, Category, Product and Customer views