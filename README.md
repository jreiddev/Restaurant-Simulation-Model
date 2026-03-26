# Restaurant-Simulation-Model
New Restaurant Proposal:
• Domain: A new real world restaurant with limited resources available.
• Problem Statement: What is the best way for newly established restaurant to manage limited resources so that profits
remain as high as possible while simultaneously ensuring customer satisfaction
• Scope: This model will focus on customer satisfaction and resource management during the time a restaurant opens to a
late afternoon shift, as this reflects one half of the day. The metrics gathered will help track inventory used, inventory cost,
and customer patience and queue abandonment. This model will NOT be focusing on extended time frames, such as a
week or month, employee satisfaction, employee wages, customer satisfaction with the meals taste, or random events such
as customer celebrations, special menu options that draw in customers (i.e. Happy Hour, Ladies Night, etc), or competing
restaurants.

3/26/26 UPDATE (Found inside M3):
The final code has been implemented. All customer patience, server/cook methods, abandonment rates, inventory usage/waste has been configured. The code now also creates a .csv and .txt file for logging information. A basic menu has been implemented to act as a user interface, allowing multiple runs with a single programs operation, the ability to configure starting metrics, and the ability to set all metrics back to the default starting parameters. 

What in the scope had to be abandoned...
-Varied menu options with different ingredients/prices/ingredient cost
-More customer reactions to different situations, turning away food cooked improperly, not liking food (in terms of sending it back for somethign else, increasing production time/food cost. This is NOT refering to customer "satisfaction" levels directly), demanding refunds, etc
-Table seating size, while table count is configured theres no count to how many each can hold, at the moment customers only come in one at a time, not in groups of varying sizes


2/26/26 Update:
The core logic has been implemented and reflects event based architecture for the project. It now utilizes specified equations to relate the general concepts/data gathered mentioned in M1.

What remains is to change the hard coded numbers into variables that relate to outside factors, ie employee utilization -> employee stress levels -> error rates -> customer satisfaction -> abandonment rates -> profit loss, and a proper UI and data export for the user. 

Cleaned up the gitlink to more refelct proper project architecture, deleted old M1 files that were individually uploaded and are now collected under Latex M1


