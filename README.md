# Kaira
Overview: 

    Kaira is an Android Mobile Application with the goal of helping users track their job application status and maintain a profile. It includes push notifications on a chosen timer, user authentication, and profile customization.
  
Pages and files:

    Login Page:
    
        (Login.java)
        
            This class takes user input of email and password and verifies it against the firebase backend, if successful it logs the user into their account.
            
    Register Page:
    
        (Register.java)
        
            This class validates user input to ensure it matches our criteria then logs the user in the firebase database.
            
    Main Page(Landing Page):
    
        (MainActivity.java)
        
            This class is the landing page and logic page for the Fragments(List,Add,Fragment) and helps navigate through the BottomNavigationView. This file also includes the onActivityResult function where the logic behind the popup boxes throughout the app is located.
    
    List Page:
        
        (ListFragment.java)
        
            This class takes the values in the job database for the logged in user id and displays them with  a color code associated with the status of the job. When a specific job is clicked it opens up a popup where the user can change the status of a job.
            
    Job Popup:
    
        (jobPopup.java)
        
            This class gets String extras passed using Intent from the JobFragment it is called from. Then it gives the user the option to change the current status of the job. Once it is selected the status of the job as well as the location of the job is passed back to the JobFragment, where the updates will be seen.
            
    Add Page:
    
        (AddFragment.java)
        
            This class allows the user to enter informationfor a job and pushes it to the firebase databasecategorized under:
 *                                                      Root
 *                                                          |___Jobs
 *                                                                  |___Current User ID
          
    Person Page:
    
        (PersonFragment.java)
        
              This class gets the user information from the database and displays it to the user. From this Fragment you are able to update the user info, update the user profile picture, and log out of the application.
              
    Person Popup:
      
        (personPopup.java)
        
            This class takes user input and updates the user information in the database.
