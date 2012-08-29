package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import model.Collection;
import model.Donor;
import model.Location;
import model.LocationType;
import model.Product;
import model.Request;
import model.TestResult;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import repository.CollectionRepository;
import repository.DonorRepository;
import repository.IssueRepository;
import repository.LocationRepository;
import repository.LocationTypeRepository;
import repository.ProductRepository;
import repository.RequestRepository;
import repository.TestResultRepository;
import repository.UsageRepository;

@Controller
public class CreateDataController {

    @Autowired
    private DonorRepository donorRepository;

    @Autowired
    private LocationTypeRepository locationTypeRepository;

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private TestResultRepository testResultRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UsageRepository usageRepository;

    Random r = new Random();

    @Autowired
    private ProductRepository productRepository;

    private static final String[] MALE_FIRST_NAMES = new String[]{"Aaron",
            "Abel", "Abraham", "Adam", "Adrian", "Al", "Alan", "Albert",
            "Alberto", "Alejandro", "Alex", "Alexander", "Alfonso", "Alfred",
            "Alfredo", "Allan", "Allen", "Alonzo", "Alton", "Alvin", "Amos",
            "Andre", "Andres", "Andrew", "Andy", "Angel", "Angelo", "Anthony",
            "Antonio", "Archie", "Armando", "Arnold", "Arthur", "Arturo",
            "Aubrey", "Austin", "Barry", "Ben", "Benjamin", "Bennie", "Benny",
            "Bernard", "Bert", "Bill", "Billy", "Blake", "Bob", "Bobby",
            "Boyd", "Brad", "Bradford", "Bradley", "Brandon", "Brendan",
            "Brent", "Brett", "Brian", "Bruce", "Bryan", "Bryant", "Byron",
            "Caleb", "Calvin", "Cameron", "Carl", "Carlos", "Carlton",
            "Carroll", "Cary", "Casey", "Cecil", "Cedric", "Cesar", "Chad",
            "Charles", "Charlie", "Chester", "Chris", "Christian",
            "Christopher", "Clarence", "Clark", "Claude", "Clay", "Clayton",
            "Clifford", "Clifton", "Clint", "Clinton", "Clyde", "Cody",
            "Colin", "Conrad", "Corey", "Cornelius", "Cory", "Courtney",
            "Craig", "Curtis", "Dale", "Dallas", "Damon", "Dan", "Dana",
            "Daniel", "Danny", "Darin", "Darnell", "Darrel", "Darrell",
            "Darren", "Darrin", "Darryl", "Daryl", "Dave", "David", "Dean",
            "Delbert", "Dennis", "Derek", "Derrick", "Devin", "Dewey",
            "Dexter", "Domingo", "Dominic", "Dominick", "Don", "Donald",
            "Donnie", "Doug", "Douglas", "Doyle", "Drew", "Duane", "Dustin",
            "Dwayne", "Dwight", "Earl", "Earnest", "Ed", "Eddie", "Edgar",
            "Edmond", "Edmund", "Eduardo", "Edward", "Edwin", "Elbert",
            "Elias", "Elijah", "Ellis", "Elmer", "Emanuel", "Emilio", "Emmett",
            "Enrique", "Eric", "Erick", "Erik", "Ernest", "Ernesto", "Ervin",
            "Eugene", "Evan", "Everett", "Felipe", "Felix", "Fernando",
            "Floyd", "Forrest", "Francis", "Francisco", "Frank", "Frankie",
            "Franklin", "Fred", "Freddie", "Frederick", "Fredrick", "Gabriel",
            "Garrett", "Garry", "Gary", "Gene", "Geoffrey", "George", "Gerald",
            "Gerard", "Gerardo", "Gilbert", "Gilberto", "Glen", "Glenn",
            "Gordon", "Grady", "Grant", "Greg", "Gregg", "Gregory",
            "Guadalupe", "Guillermo", "Gustavo", "Guy", "Harold", "Harry",
            "Harvey", "Hector", "Henry", "Herbert", "Herman", "Homer",
            "Horace", "Howard", "Hubert", "Hugh", "Hugo", "Ian", "Ignacio",
            "Ira", "Irvin", "Irving", "Isaac", "Ismael", "Israel", "Ivan",
            "Jack", "Jackie", "Jacob", "Jaime", "Jake", "James", "Jamie",
            "Jan", "Jared", "Jason", "Javier", "Jay", "Jean", "Jeff",
            "Jeffery", "Jeffrey", "Jerald", "Jeremiah", "Jeremy", "Jermaine",
            "Jerome", "Jerry", "Jesse", "Jessie", "Jesus", "Jim", "Jimmie",
            "Jimmy", "Jody", "Joe", "Joel", "Joey", "John", "Johnathan",
            "Johnnie", "Johnny", "Jon", "Jonathan", "Jonathon", "Jordan",
            "Jorge", "Jose", "Joseph", "Josh", "Joshua", "Juan", "Julian",
            "Julio", "Julius", "Justin", "Karl", "Keith", "Kelly", "Kelvin",
            "Ken", "Kenneth", "Kenny", "Kent", "Kerry", "Kevin", "Kim", "Kirk",
            "Kristopher", "Kurt", "Kyle", "Lamar", "Lance", "Larry",
            "Laurence", "Lawrence", "Lee", "Leland", "Leo", "Leon", "Leonard",
            "Leroy", "Leslie", "Lester", "Levi", "Lewis", "Lionel", "Lloyd",
            "Lonnie", "Loren", "Lorenzo", "Louis", "Lowell", "Lucas", "Luis",
            "Luke", "Luther", "Lyle", "Lynn", "Mack", "Malcolm", "Manuel",
            "Marc", "Marco", "Marcos", "Marcus", "Mario", "Marion", "Mark",
            "Marlon", "Marshall", "Martin", "Marty", "Marvin", "Mathew",
            "Matt", "Matthew", "Maurice", "Max", "Melvin", "Merle", "Michael",
            "Micheal", "Miguel", "Mike", "Milton", "Mitchell", "Morris",
            "Moses", "Myron", "Nathan", "Nathaniel", "Neal", "Neil", "Nelson",
            "Nicholas", "Nick", "Nicolas", "Noah", "Noel", "Norman", "Oliver",
            "Omar", "Orlando", "Orville", "Oscar", "Otis", "Owen", "Pablo",
            "Pat", "Patrick", "Paul", "Pedro", "Percy", "Perry", "Pete",
            "Peter", "Phil", "Philip", "Phillip", "Preston", "Rafael", "Ralph",
            "Ramiro", "Ramon", "Randal", "Randall", "Randolph", "Randy",
            "Raul", "Ray", "Raymond", "Reginald", "Rene", "Rex", "Ricardo",
            "Richard", "Rick", "Rickey", "Ricky", "Robert", "Roberto", "Robin",
            "Roderick", "Rodney", "Rodolfo", "Rogelio", "Roger", "Roland",
            "Rolando", "Roman", "Ron", "Ronald", "Ronnie", "Roosevelt", "Ross",
            "Roy", "Ruben", "Rudolph", "Rudy", "Rufus", "Russell", "Ryan",
            "Salvador", "Salvatore", "Sam", "Sammy", "Samuel", "Santiago",
            "Santos", "Saul", "Scott", "Sean", "Sergio", "Seth", "Shane",
            "Shannon", "Shaun", "Shawn", "Sheldon", "Sherman", "Sidney",
            "Simon", "Spencer", "Stanley", "Stephen", "Steve", "Steven",
            "Stewart", "Stuart", "Sylvester", "Taylor", "Ted", "Terence",
            "Terrance", "Terrell", "Terrence", "Terry", "Theodore", "Thomas",
            "Tim", "Timmy", "Timothy", "Toby", "Todd", "Tom", "Tomas",
            "Tommie", "Tommy", "Tony", "Tracy", "Travis", "Trevor", "Troy",
            "Tyler", "Tyrone", "Van", "Vernon", "Victor", "Vincent", "Virgil",
            "Wade", "Wallace", "Walter", "Warren", "Wayne", "Wendell",
            "Wesley", "Wilbert", "Wilbur", "Wilfred", "Willard", "William",
            "Willie", "Willis", "Wilson", "Winston", "Wm", "Woodrow", "Zachary"};

    private static final String[] FEMALE_FIRST_NAMES = new String[]{"Ada",
            "Adrienne", "Agnes", "Alberta", "Alexandra", "Alexis", "Alice",
            "Alicia", "Alison", "Allison", "Alma", "Alyssa", "Amanda", "Amber",
            "Amelia", "Amy", "Ana", "Andrea", "Angel", "Angela", "Angelica",
            "Angelina", "Angie", "Anita", "Ann", "Anna", "Anne", "Annette",
            "Annie", "Antoinette", "Antonia", "April", "Arlene", "Ashley",
            "Audrey", "Barbara", "Beatrice", "Becky", "Belinda", "Bernadette",
            "Bernice", "Bertha", "Bessie", "Beth", "Bethany", "Betsy", "Betty",
            "Beulah", "Beverly", "Billie", "Blanca", "Blanche", "Bobbie",
            "Bonnie", "Brandi", "Brandy", "Brenda", "Bridget", "Brittany",
            "Brooke", "Camille", "Candace", "Candice", "Carla", "Carmen",
            "Carol", "Carole", "Caroline", "Carolyn", "Carrie", "Casey",
            "Cassandra", "Catherine", "Cathy", "Cecelia", "Cecilia", "Celia",
            "Charlene", "Charlotte", "Chelsea", "Cheryl", "Christie",
            "Christina", "Christine", "Christy", "Cindy", "Claire", "Clara",
            "Claudia", "Colleen", "Connie", "Constance", "Cora", "Courtney",
            "Cristina", "Crystal", "Cynthia", "Daisy", "Dana", "Danielle",
            "Darla", "Darlene", "Dawn", "Deanna", "Debbie", "Deborah", "Debra",
            "Delia", "Della", "Delores", "Denise", "Desiree", "Diana", "Diane",
            "Dianna", "Dianne", "Dixie", "Dolores", "Donna", "Dora", "Doreen",
            "Doris", "Dorothy", "Ebony", "Edith", "Edna", "Eileen", "Elaine",
            "Eleanor", "Elena", "Elisa", "Elizabeth", "Ella", "Ellen",
            "Eloise", "Elsa", "Elsie", "Elvira", "Emily", "Emma", "Erica",
            "Erika", "Erin", "Erma", "Ernestine", "Essie", "Estelle", "Esther",
            "Ethel", "Eula", "Eunice", "Eva", "Evelyn", "Faith", "Fannie",
            "Faye", "Felicia", "Flora", "Florence", "Frances", "Francis",
            "Freda", "Gail", "Gayle", "Geneva", "Genevieve", "Georgia",
            "Geraldine", "Gertrude", "Gina", "Ginger", "Gladys", "Glenda",
            "Gloria", "Grace", "Gretchen", "Guadalupe", "Gwen", "Gwendolyn",
            "Hannah", "Harriet", "Hattie", "Hazel", "Heather", "Heidi",
            "Helen", "Henrietta", "Hilda", "Holly", "Hope", "Ida", "Inez",
            "Irene", "Iris", "Irma", "Isabel", "Jackie", "Jacqueline",
            "Jacquelyn", "Jaime", "Jamie", "Jan", "Jana", "Jane", "Janet",
            "Janice", "Janie", "Janis", "Jasmine", "Jean", "Jeanette",
            "Jeanne", "Jeannette", "Jeannie", "Jenna", "Jennie", "Jennifer",
            "Jenny", "Jessica", "Jessie", "Jill", "Jo", "Joan", "Joann",
            "Joanna", "Joanne", "Jodi", "Jody", "Johanna", "Johnnie",
            "Josefina", "Josephine", "Joy", "Joyce", "Juana", "Juanita",
            "Judith", "Judy", "Julia", "Julie", "June", "Kara", "Karen",
            "Kari", "Karla", "Kate", "Katherine", "Kathleen", "Kathryn",
            "Kathy", "Katie", "Katrina", "Kay", "Kayla", "Kelley", "Kelli",
            "Kellie", "Kelly", "Kendra", "Kerry", "Kim", "Kimberly", "Krista",
            "Kristen", "Kristi", "Kristie", "Kristin", "Kristina", "Kristine",
            "Kristy", "Krystal", "Lana", "Latoya", "Laura", "Lauren", "Laurie",
            "Laverne", "Leah", "Lee", "Leigh", "Lela", "Lena", "Leona",
            "Leslie", "Leticia", "Lila", "Lillian", "Lillie", "Linda",
            "Lindsay", "Lindsey", "Lisa", "Lois", "Lola", "Lora", "Lorena",
            "Lorene", "Loretta", "Lori", "Lorraine", "Louise", "Lucia",
            "Lucille", "Lucy", "Lula", "Luz", "Lydia", "Lynda", "Lynette",
            "Lynn", "Lynne", "Mabel", "Mable", "Madeline", "Mae", "Maggie",
            "Mamie", "Mandy", "Marcella", "Marcia", "Margaret", "Margarita",
            "Margie", "Marguerite", "Maria", "Marian", "Marianne", "Marie",
            "Marilyn", "Marion", "Marjorie", "Marlene", "Marsha", "Marta",
            "Martha", "Mary", "Maryann", "Mattie", "Maureen", "Maxine", "May",
            "Megan", "Meghan", "Melanie", "Melba", "Melinda", "Melissa",
            "Melody", "Mercedes", "Meredith", "Michele", "Michelle", "Mildred",
            "Mindy", "Minnie", "Miranda", "Miriam", "Misty", "Molly", "Mona",
            "Monica", "Monique", "Muriel", "Myra", "Myrtle", "Nadine", "Nancy",
            "Naomi", "Natalie", "Natasha", "Nellie", "Nettie", "Nichole",
            "Nicole", "Nina", "Nora", "Norma", "Olga", "Olive", "Olivia",
            "Ollie", "Opal", "Ora", "Pam", "Pamela", "Pat", "Patricia",
            "Patsy", "Patti", "Patty", "Paula", "Paulette", "Pauline", "Pearl",
            "Peggy", "Penny", "Phyllis", "Priscilla", "Rachael", "Rachel",
            "Ramona", "Raquel", "Rebecca", "Regina", "Renee", "Rhonda", "Rita",
            "Roberta", "Robin", "Robyn", "Rochelle", "Rosa", "Rosalie", "Rose",
            "Rosemarie", "Rosemary", "Rosie", "Roxanne", "Ruby", "Ruth",
            "Sabrina", "Sadie", "Sally", "Samantha", "Sandra", "Sandy", "Sara",
            "Sarah", "Shannon", "Shari", "Sharon", "Shawna", "Sheila",
            "Shelia", "Shelley", "Shelly", "Sheri", "Sherri", "Sherry",
            "Sheryl", "Shirley", "Silvia", "Sonia", "Sonja", "Sonya", "Sophia",
            "Sophie", "Stacey", "Stacy", "Stella", "Stephanie", "Sue", "Susan",
            "Susie", "Suzanne", "Sylvia", "Tabitha", "Tamara", "Tami", "Tammy",
            "Tanya", "Tara", "Tasha", "Teresa", "Teri", "Terri", "Terry",
            "Thelma", "Theresa", "Tiffany", "Tina", "Toni", "Tonya", "Tracey",
            "Traci", "Tracy", "Tricia", "Valerie", "Vanessa", "Velma", "Vera",
            "Verna", "Veronica", "Vicki", "Vickie", "Vicky", "Victoria",
            "Viola", "Violet", "Virginia", "Vivian", "Wanda", "Wendy",
            "Whitney", "Willie", "Wilma", "Winifred", "Yolanda", "Yvette",
            "Yvonne"};
    private static final String[] LAST_NAMES = new String[]{"Abbott", "Adams",
            "Adkins", "Aguilar", "Alexander", "Allen", "Allison", "Alvarado",
            "Alvarez", "Anderson", "Andrews", "Armstrong", "Arnold", "Atkins",
            "Austin", "Bailey", "Baker", "Baldwin", "Ball", "Ballard", "Banks",
            "Barber", "Barker", "Barnes", "Barnett", "Barrett", "Barton",
            "Bass", "Bates", "Beck", "Becker", "Bell", "Bennett", "Benson",
            "Berry", "Bishop", "Black", "Blair", "Blake", "Boone", "Bowen",
            "Bowers", "Bowman", "Boyd", "Bradley", "Brady", "Brewer",
            "Bridges", "Briggs", "Brock", "Brooks", "Brown", "Bryan", "Bryant",
            "Buchanan", "Burgess", "Burke", "Burns", "Burton", "Bush",
            "Butler", "Byrd", "Cain", "Caldwell", "Campbell", "Cannon",
            "Carlson", "Carpenter", "Carr", "Carroll", "Carson", "Carter",
            "Casey", "Castillo", "Castro", "Chambers", "Chandler", "Chapman",
            "Chavez", "Christensen", "Clark", "Clarke", "Clayton", "Cobb",
            "Cohen", "Cole", "Coleman", "Collier", "Collins", "Colon",
            "Conner", "Cook", "Cooper", "Copeland", "Cortez", "Cox", "Craig",
            "Crawford", "Cross", "Cruz", "Cummings", "Cunningham", "Curry",
            "Curtis", "Daniel", "Daniels", "Davidson", "Davis", "Dawson",
            "Day", "Dean", "Delgado", "Dennis", "Diaz", "Dixon", "Douglas",
            "Doyle", "Drake", "Duncan", "Dunn", "Edwards", "Elliott", "Ellis",
            "Erickson", "Estrada", "Evans", "Farmer", "Ferguson", "Fernandez",
            "Fields", "Figueroa", "Fisher", "Fitzgerald", "Fleming",
            "Fletcher", "Flores", "Flowers", "Floyd", "Ford", "Foster",
            "Fowler", "Fox", "Francis", "Frank", "Franklin", "Frazier",
            "Freeman", "French", "Fuller", "Garcia", "Gardner", "Garner",
            "Garrett", "Garza", "George", "Gibbs", "Gibson", "Gilbert", "Gill",
            "Glover", "Gomez", "Gonzales", "Gonzalez", "Goodman", "Goodwin",
            "Gordon", "Graham", "Grant", "Graves", "Gray", "Green", "Greene",
            "Greer", "Gregory", "Griffin", "Griffith", "Gross", "Guerrero",
            "Gutierrez", "Guzman", "Hale", "Hall", "Hamilton", "Hammond",
            "Hampton", "Hansen", "Hanson", "Hardy", "Harmon", "Harper",
            "Harrington", "Harris", "Harrison", "Hart", "Harvey", "Hawkins",
            "Hayes", "Haynes", "Henderson", "Henry", "Hernandez", "Herrera",
            "Hicks", "Higgins", "Hill", "Hines", "Hodges", "Hoffman", "Hogan",
            "Holland", "Holloway", "Holmes", "Holt", "Hopkins", "Horton",
            "Houston", "Howard", "Howell", "Hubbard", "Hudson", "Huff",
            "Hughes", "Hunt", "Hunter", "Ingram", "Jackson", "Jacobs", "James",
            "Jefferson", "Jenkins", "Jennings", "Jensen", "Jimenez", "Johnson",
            "Johnston", "Jones", "Jordan", "Joseph", "Keller", "Kelley",
            "Kelly", "Kennedy", "Kim", "King", "Klein", "Knight", "Lamb",
            "Lambert", "Lane", "Larson", "Lawrence", "Lawson", "Lee",
            "Leonard", "Lewis", "Lindsey", "Little", "Lloyd", "Logan", "Long",
            "Lopez", "Love", "Lowe", "Lucas", "Luna", "Lynch", "Lyons", "Mack",
            "Maldonado", "Malone", "Mann", "Manning", "Marsh", "Marshall",
            "Martin", "Martinez", "Mason", "Massey", "Mathis", "Matthews",
            "Maxwell", "May", "Mcbride", "Mccarthy", "Mccormick", "Mccoy",
            "Mcdaniel", "Mcdonald", "Mcgee", "Mcguire", "Mckenzie", "Mckinney",
            "Mclaughlin", "Medina", "Mendez", "Mendoza", "Meyer", "Miles",
            "Miller", "Mills", "Mitchell", "Montgomery", "Moody", "Moore",
            "Morales", "Moran", "Moreno", "Morgan", "Morris", "Morrison",
            "Morton", "Moss", "Mullins", "Munoz", "Murphy", "Murray", "Myers",
            "Nash", "Neal", "Nelson", "Newman", "Newton", "Nguyen", "Nichols",
            "Norman", "Norris", "Norton", "Nunez", "Obrien", "Oliver", "Olson",
            "Ortega", "Ortiz", "Osborne", "Owen", "Owens", "Padilla", "Page",
            "Palmer", "Park", "Parker", "Parks", "Parsons", "Patrick",
            "Patterson", "Patton", "Paul", "Payne", "Pearson", "Pena", "Perez",
            "Perkins", "Perry", "Peters", "Peterson", "Phelps", "Phillips",
            "Pierce", "Pittman", "Poole", "Pope", "Porter", "Potter", "Powell",
            "Powers", "Pratt", "Price", "Quinn", "Ramirez", "Ramos", "Ramsey",
            "Ray", "Reed", "Reese", "Reeves", "Reid", "Reyes", "Reynolds",
            "Rhodes", "Rice", "Richards", "Richardson", "Riley", "Rios",
            "Rivera", "Robbins", "Roberson", "Roberts", "Robertson",
            "Robinson", "Rodgers", "Rodriguez", "Rodriquez", "Rogers",
            "Romero", "Rose", "Ross", "Rowe", "Roy", "Ruiz", "Russell", "Ryan",
            "Salazar", "Sanchez", "Sanders", "Sandoval", "Santiago", "Santos",
            "Saunders", "Schmidt", "Schneider", "Schultz", "Schwartz", "Scott",
            "Sharp", "Shaw", "Shelton", "Sherman", "Silva", "Simmons", "Simon",
            "Simpson", "Sims", "Singleton", "Smith", "Snyder", "Soto",
            "Sparks", "Spencer", "Stanley", "Steele", "Stephens", "Stevens",
            "Stevenson", "Stewart", "Stokes", "Stone", "Strickland",
            "Sullivan", "Summers", "Sutton", "Swanson", "Tate", "Taylor",
            "Terry", "Thomas", "Thompson", "Thornton", "Todd", "Torres",
            "Townsend", "Tran", "Tucker", "Turner", "Tyler", "Underwood",
            "Valdez", "Vargas", "Vasquez", "Vaughn", "Vega", "Wade", "Wagner",
            "Walker", "Wallace", "Walsh", "Walters", "Walton", "Ward",
            "Warner", "Warren", "Washington", "Waters", "Watkins", "Watson",
            "Watts", "Weaver", "Webb", "Weber", "Webster", "Welch", "Wells",
            "West", "Wheeler", "White", "Wilkerson", "Wilkins", "Williams",
            "Williamson", "Willis", "Wilson", "Wise", "Wolfe", "Wong", "Wood",
            "Woods", "Wright", "Yates", "Young", "Zimmerman"};
    private String[] productTypes = new String[]{"rcc", "ffp", "wholeBlood", "platelets", "partialPlatelets"};
    private String[] bloodTypes = new String[]{"A", "B", "AB", "O"};
    private String[] rhd = new String[]{"positive", "negative"};

    @RequestMapping("/admin-createData")
    public ModelAndView createDataPage(HttpServletRequest request) {


        ModelAndView modelAndView = new ModelAndView("createData");
        return modelAndView;
    }

    @RequestMapping("/admin-createDummyData")
    public ModelAndView createDummyData(@RequestParam Map<String, String> params, HttpServletRequest request) {

        String collectionNumber = params.get("collectionNumber");
        String productNumber = params.get("productNumber");
        String requestNumber = params.get("requestNumber");
        String donorNumber = params.get("donorNumber");
        createLocationTypes();
        createLocations();
        createDonors(Integer.parseInt(donorNumber));
        createCollectionsWithTestResults(Integer.parseInt(collectionNumber));
        createProducts(Integer.parseInt(productNumber));
        createRequests(Integer.parseInt(requestNumber));

        ModelAndView modelAndView = new ModelAndView("createData");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("dataCreated", true);
        model.put("hadDetails", true);
        model.put("collectionNumber", collectionNumber);
        model.put("productNumber", productNumber);
        model.put("requestNumber", requestNumber);
        model.put("donorNumber", donorNumber);
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    @RequestMapping("/admin-deleteDummyData")
    public ModelAndView deleteData(HttpServletRequest request) {

        requestRepository.deleteAllRequests();
        productRepository.deleteAllProducts();
        testResultRepository.deleteAllTestResults();
        locationRepository.deleteAllLocations();
        locationTypeRepository.deleteAllLocationTypes();
        donorRepository.deleteAllDonors();
        collectionRepository.deleteAllCollections();
        usageRepository.deleteAllUsages();
        issueRepository.deleteAllIssues();

        ModelAndView modelAndView = new ModelAndView("createData");
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("dataDeleted", true);
        modelAndView.addObject("model", model);
        return modelAndView;
    }

    private void createDonors(int donorNumber) {
        String bloodTypes[] = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        for (int i = 0; i < donorNumber; i++) {
            String firstName = "";
            String gender = "";
            if (i % 2 == 0) {
                firstName = MALE_FIRST_NAMES[r.nextInt(MALE_FIRST_NAMES.length)];
                gender = "male";
            } else {
                firstName = FEMALE_FIRST_NAMES[r.nextInt(FEMALE_FIRST_NAMES.length)];
                gender = "female";
            }
            String lastName = LAST_NAMES[r.nextInt(LAST_NAMES.length)];
            donorRepository.saveDonor(new Donor(new Integer(i + 1).toString(), firstName, lastName, gender, bloodTypes[r.nextInt(bloodTypes.length)], getRandomDOB(), null, "address" + i, Boolean.FALSE));
        }

    }

    private Date getRandomDOB() {
        try {
            return getRandomDate(new SimpleDateFormat("dd MM yyyy").parse("01 01 1970"), new SimpleDateFormat("dd MM yyyy").parse("01 01 1990"));
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    private Date getRandomDate(Date fromDate, Date toDate) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        long decFrom = cal.getTimeInMillis();
        cal.setTime(toDate);
        long decTo = cal.getTimeInMillis();
        long diff = decTo - decFrom;
        long randomOffset = r.nextLong() % diff;
        Date date = new Date(decFrom + randomOffset);
        return date;
    }

    private void createLocations() {
        List<LocationType> allLocationTypes = locationTypeRepository.getAllLocationTypes();
        String[] locationNames = {"Lusaka", "Ndola", "Kitwe", "Kabwe", "Chingola", "Mufulira", "Livingstone", "Luanshya ", "Kasama", "Chipata"};
        for (String locationName : locationNames) {
            locationRepository.saveLocation(new Location(locationName, getRandomLocationType(allLocationTypes).getLocationTypeId(), getRandomBooleanValue(), getRandomBooleanValue(), getRandomBooleanValue(), getRandomBooleanValue(), Boolean.FALSE));
        }

    }

    private boolean getRandomBooleanValue() {
        return r.nextBoolean();
    }

    private LocationType getRandomLocationType(List<LocationType> allLocationTypes) {
        return allLocationTypes.get(r.nextInt(allLocationTypes.size()));
    }


    private void createLocationTypes() {
        List<String> locationTypeNames = new ArrayList<String>();
        locationTypeNames.add("hospital");
        locationTypeNames.add("school");
        locationTypeNames.add("church");
        locationTypeNames.add("mall");
        locationTypeNames.add("government building");
        for (String locationTypeName : locationTypeNames) {
            locationTypeRepository.saveLocationType(new LocationType(locationTypeName, Boolean.FALSE));
        }
    }


    private Date getRandomCollectionDate() {
//        Date twoYearsAgo = new DateTime().minusYears(2).toDate();
//        return getRandomDate(twoYearsAgo, new Date());
        Date thrityfiveDaysAgo = new DateTime().minusDays(35).toDate();
        return getRandomDate(thrityfiveDaysAgo, new Date());
    }

    private void createCollectionsWithTestResults(int collectionNumber) {
        List<Location> sites = locationRepository.getAllCollectionSites();
        List<Location> centers = locationRepository.getAllCenters();
        List<Donor> donors = donorRepository.getAllDonors();
        String[] donorTypes = {"family", "voluntary", "other"};
        for (int i = 0; i < collectionNumber; i++) {
            Collection collection = new Collection(new Integer(i + 1).toString(), centers.get(r.nextInt(centers.size())).getLocationId(), sites.get(r.nextInt(sites.size())).getLocationId(), getRandomCollectionDate(), (long) (r.nextInt(5000)), (long) (r.nextInt(5000)), donors.get(r.nextInt(donors.size())).getDonorNumber(), donorTypes[r.nextInt(donorTypes.length)], "comment" + i, bloodTypes[r.nextInt(bloodTypes.length)], rhd[r.nextInt(rhd.length)], Boolean.FALSE);
            collectionRepository.saveCollection(collection);
            TestResult testResult = new TestResult(collection.getCollectionNumber(), collection.getDateCollected(), new DateTime(collection.getDateCollected()).plusDays(1).toDate(), "comment" + i, getRandomTestResult(), getRandomTestResult(), getRandomTestResult(), getRandomTestResult(), collection.getAbo(), collection.getRhd(), Boolean.FALSE);
            testResultRepository.saveTestResult(testResult);
        }
    }

    private void createProducts(int productNumber) {
        List<Collection> collections = collectionRepository.getAllCollections();
        for (int i = 0; i < productNumber; i++) {
            Product product = new Product(new Integer(i + 1).toString(), collections.get(r.nextInt(collections.size())), productTypes[r.nextInt(productTypes.length)], Boolean.FALSE, Boolean.FALSE);
            productRepository.saveProduct(product);
        }
    }

    private void createRequests(int requestNumber) {
        String[] status = new String[]{"pending", "partiallyFulfilled", "fulfilled"};

        List<Location> sites = locationRepository.getAllCollectionSites();

        for (int i = 0; i < requestNumber; i++) {
            Date requestDate = getRandomCollectionDate();
            Date requiredDate = new DateTime(requestDate).plusDays(15).toDate();
            String productType = productTypes[r.nextInt(productTypes.length)];
            productType = productType.equals("partialPlatelets") ? "platelets" : productType;
            Request request = new Request(new Integer(i + 1).toString(), requestDate, requiredDate, sites.get(r.nextInt(sites.size())).getLocationId(), productType, bloodTypes[r.nextInt(bloodTypes.length)], rhd[r.nextInt(rhd.length)], r.nextInt(20), "comment" + (i + 1), status[r.nextInt(status.length)], Boolean.FALSE);
            requestRepository.saveRequest(request);
        }
    }

    private String getRandomTestResult() {
        return r.nextBoolean() == true ? "reactive" : "negative";
    }


}
