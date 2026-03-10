# *Module 4 Maintainability & OO Principles*
## Reflection 1
1. TDD forces me to write down test before even writing down the skeleton
of the class. This makes me think about the design of the class and its methods
before I even write the code. Without TDD, I usually make the skeleton first, then 
guess what the behaviour of the methods will be. 
With TDD, I have to actually plan what the methods will do, and how they will interact with each other.
This really helps me to plan for the class I'm about to implement.
More than that, the tests written in the beginning of TDD are really helpful when
developing the class, since I can just run the tests to check if the class is
properly implemented or not. Even though it is a bit time and mentally consuming, 
it really helps development efficiency in the long run. 
From here, I learned that when making a class, instead of guessing or assuming how it works, 
I should plan it out first by writing tests.


2. My tests have implemented F.I.R.S.T principles. Each test
is only focused on one specific behaviour/method of a class. This way, if a test fails, I can easily identify which method is not working properly.
This also helps in making the tests more readable and faster to run. Each test is also independent, meaning that it doesn't rely on the result of another test. This way, I can run each test separately without worrying about the order of tests. The tests are also repeatable, since they don't rely on any external factors such as database or file system. This way, I can run the tests multiple times and get the same results. The tests are also self-validating, since they have assertions that check if the expected results are met. 
This way, I can easily identify if a test has passed or failed just by looking at the assertions, which means the tests are self-validating.
The tests are also repeatable, since they don't rely on external factors such as databases because they use mocks.
Meaning that I can run the tests multiple times and get the same results. 
The tests are also timely, since they are made before the actual implementation of the code. 
This ensures that the code is design to be able to be tested.






# *Module 3 Maintainability & OO Principles*
## Reflection
### SRP
- CarRepository is supposed to only handle connections with the database (in this case a list). However, the previous code
includes the logic for inserting UUID into the Car object that is to be saved in the database. This adds another responsibility
to CarRepository's method called create. To fix this problem, I separated the UUID insertion logic into 
CarServiceImpl, whose responsibility is suited more for business logic. By doing this, CarRepository can keep its single
responsibility of handling storage details. I also separated the logic for creating the UUID into a class called UuidGenerator.
This helps separate the responsibility for creating and inserting the UUID.


- Before, the class ProductController is extended by CarController. This makes CarController
inherit all of ProductController's methods, which aren't necessary for CarController's logic. 
This only mixes CarController's logic with ProductController, which gives it more responsibility.
This clearly violates SRP, which states that classes should have single responsibility.
To fix this problem, I separated the logic for Product and Car controller. Now we have
the new class called CarController which specifically does the logic for Car's controller and doesn't extend ProductController.

### OCP
- The previous version of CarController and ProductController also violates OCP. Both of them have specific method names
and endpoints. This prevents them from being Open to Extensions. To enable the Open principle, I changed the endpoint so that
it has a format that can be extended. 

### LSP
- The same problem arises in CarController that extends ProductController, it also violates LSP. The logic of CarController 
differs from ProductController. It technically can substitute ProductController since it extends all the methods, but the CarControllers
methods can work if it is substitutes ProductController. This may result CarController logic working in a Product feature, which is not intended.
To solve this, CarController no longer extends ProductController. Now, CarController is on its own.

### ISP
- The interfaces don't violate ISP. Each interface provides the required methods, and each class that implements it
implements those methods nicely. No classes are forced to implement methods in a way that the implemented methods does nothing.


### DIP
- CarService used to reference the concrete class CarServiceImpl, which does not abide to DIP. I fixed this problem
by changing the reference to the interface CarService instead.

### Advantages
- By separating responsibilities, we can easily reuse methods and fix errors. For example, I separated the logic for making UUID in 
CarServiceImpl. CarServiceImpl now just inserts the UUID, while the class UuidGenerator creates the UUID. This way, we can make tests for 
UuidGenerator to see if it actually creates intended UUID formats. 
- Applying OCP really helps in expanding existing features into new ones. For example, the class Car is already open for extension. We can easily
extend it into a new class such as RaceCar which can inherit Car's variables but then add a variable of its own such as maxSpeed. 
- LSP also works well when we want to add new classes. In this project, there are no classes that extends other classes in a way that
it became a new feature. But if we were to add a new feature and apply LSP, we can easily substitute their parent classes with this new feature if needed, and
our program will work just fine.
- Interface segregation so far isn't a problem in this project. Since all classes implements the methods properly. For example CarServiceImpl implements
all CarService methods properly, not a single method left unused and logic is still safe. This way, interfaces are used properly and their methods are all used.
- DIP is really convenient to have in a project. For example, CarController references CarService instead of CarServiceImpl. This makes the class loosely coupled and
more flexible to change. Referencing to a lower level module such as interfaces gives more room and flexibility for the concrete class to make changes and make more methods, while preserving the business logic.
### Disadvantages
- If we don't separate the responsibility of methods or classes, then we would have a hard time testing the code. For example, if the logic
for creating and inserting UUID is not separated, then if we made a mistake in creating the UUID, then it would be a hassle to check, since there is no direct method that we can test to see the created UUID.
- If we don't apply OCP on our classes, extending into a new feature will be difficult. We can't reuse methods if they don't implement OCP. For example, 
- If we don't apply LSP, adding a new feature can be exhausting, especially if it replaces a certain class. It may result in unintended logical errors.
- Not implementing ISP will make classes implement methods that are not necessary for them. There will be methods with no logic, and this can lead to unintended logical errors, especially
in a collaborative environment, where others might think the method actually does something, even though it's not properly implemented in the class.
- Not implementing DIP can result in classes being tightly coupled by referencing concrete classes. Changes made to a class are not that flexible anymore. Other than that, unit testing becomes more
complicated since the code is tied to more details, making it hard to isolate for tests.

# *Module 2 CI/CD & DevOps*
Deployed url: https://flat-nat-a-samuel-marcelino-tindaon-2406435830-7209c7d2.koyeb.app/product/list
## Reflection
### Code Quality Issues
When using sonarqube, one of the code quality issues that I got was a security hotspot on my sonarqube CI pipeline. 
- Before, I wrote:
```bash
- name: Run SonarCloud analysis
        uses: SonarSource/sonarcloud-github-action@v2n
```
In this case, sonarqube said that stating @v2 was a security hotspot because:
- It is executing a third-party code
- It is pulled from GitHub
- It is inside a CI environment
- It may have access to the secret SONAR_TOKEN

A security hotspot by itself is not a vulnerability. However, it is a warning: running a third-party code has its own risks. The tag @v2 means that the external code I'm using may be subject to change and I won't be notified if I didn't check. This means that if the third-party code changes, it may cause unexpected errors in my repository. Worst case scenario is that the changes are malicious codes.

The solution sonarqube gave me is use the SHA that points directly to the commit/release of that third-party code:
```bash
- name: Run SonarCloud analysis
        uses: SonarSource/sonarcloud-github-action@ffc3010689be73b8e5ae0c57ce35968afd7909e8
```
This way, it guarantees that the third-party code that I'm using will not change and always point to that exact commit/release of that third-party code. Therefore, I won't have to worry if @v2 has changes to it.

### CI/CD workflow review
Based on the CI/CD workflows  currently have, I think they already implemented Continuous Integration and Continuous Deployment. First of all, tests are always ran on everytime changes are made. This automates testing and prevents human errors such as forgetting to test changes. Other than than, code quality is also checked by sonarqube and ossf scorecard. This means any code vulnerabilities, security hotspots, and code cleanliness are also checked everytime a change is commited. 

Continuous deployment is implemented on the branch main. It will automatically redeploy the project to Koyeb when changes are made in main. However, those changes are guarded by the CI before. This means if the changes failed the CI, then the project won't be deployed. 


# *Module 1 Coding Standards*

---
## Reflection 1
Clean code implementations:
- Meaningful Names. Variables are named in a way that represents the value it holds. Such as the following:
    ```java
    Product productToDelete = findById(productId);
    ``` 
    This variable acts as a placeholder for a Product class that is going to be deleted after a finding the product in the repository. It Doesn't need a comment to explain this, since the variable's name itself is self-explanatory.
- Functions, for example:
  ```java
  public void delete(String productId) {
        Product productToDelete = findById(productId);

        if (productToDelete != null) {
            productData.remove(productToDelete);
        }
    }
  ```
  The method's name is self-explanatory and it only does one task which is to delete. 
- There are no unnecessary comments. Using clean code promotes the clarity of variable names and function separation. This leads to a code that is self-explanatory and doesn't require comments to understand. 
- Layout and Formatting are done using indentations and separation of code blocks using blank lines. This makes the program much more readable.
- However, I haven't properly implemented error handling such as try-catches. Some of my methods still return null which can cause unexpected errors. This can be improved by using Optional classes to avoid returning null values.
## Reflection 2
- Unit tests should be done in a way that it covers all public methods of a class. If a method has various results, then we should make separate tests that exercises each distinct result of that method. That way, we can have increased code coverage. However, 100% code coverage doesn't mean that our code has no bugs or errors. Code coverage indicates the lines of code that are executed during testing. Correctness depends on what we wrote on the unit test. Human error may be present when writing down unit test assertions. Sometimes we can make wrong logical assertions, which would result in a passed test while having incorrect or unintended behaviour.   
- Since the new class has the exact same setup procedures and instance variables, the code won't adhere to clean code principles. The identical setup makes the code redundant. This issue can be avoided by checking the quantity of the product in the original file CreateProductFunctionalTest.java. We just need to add a few lines of code to verify the quantity. Creating a new Java class just to verify quantity is unnecessary. 
