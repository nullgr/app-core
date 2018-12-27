# 1.1.0-alpha.4
* Implement ***core-security*** module
* [#51] - Fixed API version checking for WindowExtensions
* [#54] - Added functions to convert Date to Timestamp and compare fields of Date

# 1.0.2-alpha.3
* [#47] 
    - Fix EditText.applyFilterAllowedDigits behavior
* [#48] 
    - Add new extensions and classes for EditText:
    - Added extension```EditText.applyFilters(bindFunction: ArrayList<InputFilter>.() -> Unit)```
    - Added extensions```EditText.doOnTextChanged```, ```EditText.doBeforeTextChanged```, ```EditText.doAfterTextChanged``` 
    and ```EditText.addTextChangedListener(onTextChanged: ((text: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null,
                                               afterTextChanged: ((editable: Editable?) -> Unit)? = null,
                                               beforeTextChanged: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null):TextWatcher```
    - Added extension```EditText.makeSecure()```
    - Added extension ```EditText.toggleSecure()```
    - Created class ```AllowedCharactersInputFilter```
    - Created class ```OnlyDigitsInputFilter```
    - Created class ```OnlyLettersOrDigitsInputFilter```
    - Added fabric methods to create such input filters as:
     ```AllowedCharactersInputFilter```,```OnlyDigitsInputFilter```,
     ```OnlyLettersOrDigitsInputFilter```, ```IntentFilter.AllCaps```,
     ```IntentFilter.LengthFilter```
    - Created abstract class ```SimpleTextWatcher``` 
# 1.0.1-alpha.2 (Jul 11, 2018)
* [#33] - Fixed typo at SupportedFeaturesExtensions
* Update kotlin and gradle version
* [#40] - Fix addScreen and replaceScreen functions 
(pass tag to addToBackStack function instead of **null**)
* [#38] - Improved DynamicAdapter
* [#43] - Change min version of API
* [#45] - Fix DynamicAdapter updateItems

# 1.0.0-alpha.1 (Jun 5, 2018)

* Initial release