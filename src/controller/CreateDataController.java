package controller;

import backingform.DonationBackingForm;
import backingform.ComponentBackingForm;
import backingform.RequestBackingForm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import model.address.Address;
import model.address.Contact;
import model.bloodbagtype.BloodBagType;
import model.bloodtesting.TTIStatus;
import model.bloodtesting.rules.BloodTestingRule;
import model.bloodtesting.rules.DonationField;
import model.component.Component;
import model.donation.Donation;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.location.Location;
import model.producttype.ProductType;
import model.request.Request;
import model.requesttype.RequestType;
import model.util.Gender;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import repository.BloodBagTypeRepository;
import repository.DonationRepository;
import repository.DonationTypeRepository;
import repository.DonorRepository;
import repository.GenericConfigRepository;
import repository.LocationRepository;
import repository.ComponentRepository;
import repository.ProductTypeRepository;
import repository.RequestRepository;
import repository.RequestTypeRepository;
import repository.SequenceNumberRepository;
import repository.bloodtesting.BloodTestingRepository;
import utils.CustomDateFormatter;

@RestController
@RequestMapping("createdata")
public class CreateDataController {

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private DonationTypeRepository donorTypeRepository;

  @Autowired
  private BloodBagTypeRepository bloodBagTypeRepository;

  @Autowired
  private ProductTypeRepository productTypeRepository;

  @Autowired
  private RequestTypeRepository requestTypeRepository;
  
  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;

  @Autowired
  private GenericConfigRepository genericConfigRepository;

  @Autowired
  private SequenceNumberRepository sequenceNumberRepository;

  Random random = new Random();

  private static final String[] cities = {"Lusaka", "Ndola", "Kitwe", "Kabwe",
                                          "Chingola", "Livingstone",
                                          "Luanshya", "Kasama", "Chipata"};

  @Autowired
  private ComponentRepository componentRepository;

  private static final String[] MALE_FIRST_NAMES = new String[] { "Aaron",
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
      "Willie", "Willis", "Wilson", "Winston", "Wm", "Woodrow", "Zachary" };

  private static final String[] FEMALE_FIRST_NAMES = new String[] { "Ada",
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
      "Yvonne" };
  private static final String[] LAST_NAMES = new String[] { "Abbott",
      "Adams", "Adkins", "Aguilar", "Alexander", "Allen", "Allison",
      "Alvarado", "Alvarez", "Anderson", "Andrews", "Armstrong",
      "Arnold", "Atkins", "Austin", "Bailey", "Baker", "Baldwin", "Ball",
      "Ballard", "Banks", "Barber", "Barker", "Barnes", "Barnett",
      "Barrett", "Barton", "Bass", "Bates", "Beck", "Becker", "Bell",
      "Bennett", "Benson", "Berry", "Bishop", "Black", "Blair", "Blake",
      "Boone", "Bowen", "Bowers", "Bowman", "Boyd", "Bradley", "Brady",
      "Brewer", "Bridges", "Briggs", "Brock", "Brooks", "Brown", "Bryan",
      "Bryant", "Buchanan", "Burgess", "Burke", "Burns", "Burton",
      "Bush", "Butler", "Byrd", "Cain", "Caldwell", "Campbell", "Cannon",
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
      "Woods", "Wright", "Yates", "Young", "Zimmerman" };

  /**
   * 
   * #209 - method do nothing
   *
  @RequestMapping("/admin-createData")
  @PreAuthorize("hasRole('"+PermissionConstants.MANAGE_DATA_SETUP+"')")
  @Deprecated
  public ModelAndView createDataPage(HttpServletRequest request) {

    ModelAndView modelAndView = new ModelAndView("createData");
    return modelAndView;
  }
  */

  public void createDonors(int numDonors) {

    List<Donor> donors = new ArrayList<Donor>();
    for (int i = 0; i < numDonors; i++) {
      String firstName = "";
      Gender gender = Gender.male;
      if (i % 2 == 0) {
        firstName = MALE_FIRST_NAMES[random.nextInt(MALE_FIRST_NAMES.length)];
        gender = Gender.male;
      } else {
        firstName = FEMALE_FIRST_NAMES[random.nextInt(FEMALE_FIRST_NAMES.length)];
        gender = Gender.female;
      }
      String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
      Donor donor = new Donor();
      Address address = new Address();
      Contact contact = new Contact();
      address.setHomeAddressLine1("address " + i);
      address.setHomeAddressCity(cities[random.nextInt(cities.length)]);
      address.setHomeAddressCountry("Zambia");
      donor.setAddress(address);
      donor.setDonorNumber(sequenceNumberRepository.getNextDonorNumber());
      donor.setFirstName(firstName);
      donor.setLastName(lastName);
      donor.setGender(gender);
      donor.setBirthDate(getRandomBirthDate());
      donor.setNotes("notes " + i);
      donor.setIsDeleted(false);
      donors.add(donor);
    }
    donorRepository.addAllDonors(donors);

  }

  private Date getRandomBirthDate() {
    try {
      return getRandomDate(
          new SimpleDateFormat("dd MM yyyy").parse("01 01 1970"),
          new SimpleDateFormat("dd MM yyyy").parse("01 01 1990"));
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
    long randomOffset = 0;
    while (true) {
      randomOffset = random.nextLong() % diff;
      if (randomOffset > diff/2 && random.nextBoolean())
        break;
    }
    Date date = new Date(decFrom + randomOffset);
    return date;
  }

  private Date getRandomDonationDate() {
    // Date twoYearsAgo = new DateTime().minusYears(2).toDate();
    // return getRandomDate(twoYearsAgo, new Date());
    Date twoYearsAgo = new DateTime().minusDays(300).toDate();
    return getRandomDate(twoYearsAgo, new Date());
  }

  private Date getRandomRequestDate() {
    // Date twoYearsAgo = new DateTime().minusYears(2).toDate();
    // return getRandomDate(twoYearsAgo, new Date());
    Date twoYearsAgo = new DateTime().minusDays(100).toDate();
    return getRandomDate(twoYearsAgo, new Date());
  }

  void createDonationsWithTestResults(int numDonations) {
    List<Location> sites = locationRepository.getAllDonorPanels();
    List<Location> centers = locationRepository.getAllDonorPanels();
    List<Donor> donors = donorRepository.getAllDonors();
    List<DonationType> donationTypes = donorTypeRepository.getAllDonationTypes();

    List<Donation> donations = new ArrayList<Donation>();

    List<BloodBagType> bloodBagTypes = bloodBagTypeRepository.getAllBloodBagTypes();
    List<String> donationIdentificationNumbers = sequenceNumberRepository.getBatchDonationIdentificationNumbers(numDonations);
    for (int i = 0; i < numDonations; i++) {
      DonationBackingForm donation = new DonationBackingForm();
      donation.setDonationIdentificationNumber(donationIdentificationNumbers.get(i));    

      donation.setPackType(bloodBagTypes.get(Math.abs(random.nextInt()) % bloodBagTypes.size()));

      String donationDate = CustomDateFormatter.getDateTimeString(getRandomDonationDate());
      donation.setDonationDate(donationDate);
      donation.setDonor(donors.get(Math.abs(random.nextInt()) % donors.size()));
      donation.setNotes("notes sample " + i);
      donation.setDonationType(donationTypes.get(Math.abs(random.nextInt()) % donationTypes.size()));
      donation.setIsDeleted(false);

      donations.add(donation.getDonation());
    }

    donationRepository.addAllDonations(donations);

    addTestResultsForDonations(donations);
  }

  private void addTestResultsForDonations(
      List<Donation> donations) {
    addBloodTypingResultsForDonations(donations);
    addTTIResultsForDonations(donations);
  }

  public void createProducts(int numProducts) {
    List<Donation> donations = donationRepository.getAllDonations();
    List<ProductType> productTypes = productTypeRepository.getAllProductTypes();
    List<Component> components = new ArrayList<Component>();
    for (int i = 0; i < numProducts; i++) {
      Donation c = donations.get(random.nextInt(donations.size()));
      Component p = new ComponentBackingForm(true).getComponent();
      p.setDonation(c);
      p.setProductType(productTypes.get(random.nextInt(productTypes.size())));
      Date d = c.getDonationDate();
      p.setCreatedOn(d);
      Calendar cal = Calendar.getInstance();
      cal.setTime(d);
      cal.add(Calendar.DATE, 35);
      p.setExpiresOn(cal.getTime());
      p.setIsDeleted(false);
      components.add(p);
    }
    componentRepository.addAllComponents(components);
  }

  private void addBloodTypingResultsForDonations(List<Donation> donations) {

    Map<String, List<BloodTestingRule>> bloodAboRuleMap = new HashMap<String, List<BloodTestingRule>>();
    Map<String, List<BloodTestingRule>> bloodRhRuleMap = new HashMap<String, List<BloodTestingRule>>();
    for (BloodTestingRule rule : bloodTestingRepository.getBloodTypingRules(true)) {
      switch (rule.getDonationFieldChanged()) {
        case BLOODABO: if (!bloodAboRuleMap.containsKey(rule.getNewInformation())) {
                         bloodAboRuleMap.put(rule.getNewInformation(), new ArrayList<BloodTestingRule>());
                       }
                       bloodAboRuleMap.get(rule.getNewInformation()).add(rule);
                       break;
        case BLOODRH:  if (!bloodRhRuleMap.containsKey(rule.getNewInformation())) {
                         bloodRhRuleMap.put(rule.getNewInformation(), new ArrayList<BloodTestingRule>());
                       }
                       bloodRhRuleMap.get(rule.getNewInformation()).add(rule);
                       break;
      }
    }

    Map<String, String> createDataProperties = genericConfigRepository.getConfigProperties("createData");

    double leaveOutDonationsPercentage = Double.parseDouble(createDataProperties.get("leaveOutDonationsProbability"));
    double incorrectBloodTypePercentage = Double.parseDouble(createDataProperties.get("incorrectBloodTypeProbability"));

    List<String> aboValues = new ArrayList<String>(bloodAboRuleMap.keySet());
    List<String> rhValues = new ArrayList<String>(bloodRhRuleMap.keySet());

    Map<Long, Map<Long, String>> testResults = new HashMap<Long, Map<Long,String>>();

    Random generator = new Random();

    for (Donation donation : donations) {

      if (Math.random() < leaveOutDonationsPercentage) {
        // do not add results for a small fraction of the donations
        continue;
      }

      String bloodAbo = "";
      String bloodRh = "";
      if (donation.getDonor() == null) {
        bloodAbo = donation.getDonor().getBloodAbo();
        bloodRh = donation.getDonor().getBloodRh();
      }
      if (StringUtils.isBlank(bloodAbo) || StringUtils.isBlank(bloodRh)) {
        // first donation for this donor
        bloodAbo = aboValues.get(generator.nextInt(aboValues.size()));
        bloodRh = rhValues.get(generator.nextInt(rhValues.size()));
      }

      if (Math.random() < incorrectBloodTypePercentage) {
        // with a small probability choose a different blood group
        bloodAbo = aboValues.get(generator.nextInt(aboValues.size()));
        bloodRh = rhValues.get(generator.nextInt(rhValues.size()));
      }

      List<BloodTestingRule> aboRules = bloodAboRuleMap.get(bloodAbo);
      List<BloodTestingRule> rhRules = bloodRhRuleMap.get(bloodRh);
      // ideally this should not be empty but since we allow the user to configure the rules
      // lets just handle this case
      if (aboRules == null || aboRules.isEmpty() ||
          rhRules == null || rhRules.isEmpty())
        continue;

      BloodTestingRule aboRule = aboRules.get(generator.nextInt(aboRules.size()));
      BloodTestingRule rhRule = rhRules.get(generator.nextInt(rhRules.size()));

      // we know which rule to apply
      Map<Long, String> testResultsForDonation = new HashMap<Long, String>();
      testResults.put(donation.getId(), testResultsForDonation);

      int index = 0;
      String[] patternResults = aboRule.getPattern().split(",");
      for (String testId : aboRule.getBloodTestsIds().split(",")) {
        if (StringUtils.isBlank(testId) ||
            index >= patternResults.length ||
            StringUtils.isBlank(patternResults[index]))
          continue;
        testResultsForDonation.put(Long.parseLong(testId), patternResults[index]);
        index++;
      }

      index = 0;
      patternResults = rhRule.getPattern().split(",");
      for (String testId : rhRule.getBloodTestsIds().split(",")) {
        if (StringUtils.isBlank(testId) ||
            index >= patternResults.length ||
            StringUtils.isBlank(patternResults[index]))
          continue;
        testResultsForDonation.put(Long.parseLong(testId), patternResults[index]);
        index++;
      }
    }

    bloodTestingRepository.saveBloodTestingResults(testResults, true);
  }

  @SuppressWarnings("unchecked")
  private void addTTIResultsForDonations(List<Donation> donations) {

    Map<Long, String> donationIdentificationNumberMap = new HashMap<Long, String>();
    for (Donation c : donations) {
      donationIdentificationNumberMap.put(c.getId(), c.getDonationIdentificationNumber());
    }

    Map<String, List<BloodTestingRule>> ttiRuleMap = new HashMap<String, List<BloodTestingRule>>();
    for (BloodTestingRule rule : bloodTestingRepository.getTTIRules(true)) {
      if (rule.getDonationFieldChanged().equals(DonationField.TTISTATUS)) {
        if (!ttiRuleMap.containsKey(rule.getNewInformation())) {
          ttiRuleMap.put(rule.getNewInformation(), new ArrayList<BloodTestingRule>());
        }
        ttiRuleMap.get(rule.getNewInformation()).add(rule);
      }
    }

    Map<String, String> createDataProperties = genericConfigRepository.getConfigProperties("createData");
    double leaveOutDonationsPercentage = Double.parseDouble(createDataProperties.get("leaveOutDonationsProbability"));
    double unsafePercentage = Double.parseDouble(createDataProperties.get("unsafeProbability"));

    Set<String> allBloodTestsIds = new HashSet<String>();
    
    Map<Long, Map<Long, String>> testResults = new HashMap<Long, Map<Long,String>>();
    Random generator = new Random();

    for (Donation donation : donations) {

      if (Math.random() < leaveOutDonationsPercentage) {
        // do not add results for a small fraction of the donations
        continue;
      }

      TTIStatus ttiStatus = TTIStatus.TTI_SAFE;
      
      if (Math.random() < unsafePercentage) {
        // with a small probability choose a different blood group
        ttiStatus = TTIStatus.TTI_UNSAFE;
      }

      List<BloodTestingRule> ttiRules = ttiRuleMap.get(ttiStatus.toString());
      // ideally this should not be empty but since we allow the user to configure the rules
      // lets just handle this case
      if (ttiRules == null || ttiRules.isEmpty())
        continue;

      BloodTestingRule ttiRule = ttiRules.get(generator.nextInt(ttiRules.size()));

      // we know which rule to apply
      Map<Long, String> testResultsForDonation = new HashMap<Long, String>();
      testResults.put(donation.getId(), testResultsForDonation);

      int index = 0;
      String[] patternResults = ttiRule.getPattern().split(",");
      for (String testId : ttiRule.getBloodTestsIds().split(",")) {
        if (StringUtils.isBlank(testId) ||
            index >= patternResults.length ||
            StringUtils.isBlank(patternResults[index]))
          continue;
        allBloodTestsIds.add(testId);
        testResultsForDonation.put(Long.parseLong(testId), patternResults[index]);
        index++;
      }
    }

    // these test results are grouped by donation, put them on a plate
    // so that we can record machine readings also

    // for each test we will have a separate map of the plate
    Map<String, Object> ttiPlatesByTest = null;

    Integer maxColumns = 12;
    Integer maxRows = 8;
    // initialize to maximum so that the map is initialized
    Integer rowNum = maxRows;
    Integer colNum = maxColumns;

    for (Long donationId : testResults.keySet()) {
      Map<Long, String> testResultsForDonation = testResults.get(donationId);
      for (Long testId : testResultsForDonation.keySet()) {
        if (rowNum == maxRows && colNum == maxColumns) {
          // save the data on the plate

          if (ttiPlatesByTest != null) {
            for (String t : allBloodTestsIds) {
              bloodTestingRepository.saveTTIResultsOnPlate((Map<String, Map<String, Object>>) ttiPlatesByTest.get(t), Long.parseLong(t));
            }
          }

          rowNum = 1;
          colNum = 1;

          ttiPlatesByTest = new HashMap<String, Object>();
          for (String t : allBloodTestsIds) {
            Map<String, Map<String, Object>> plateData = new HashMap<String, Map<String,Object>>();
            for (int i = 1; i <= maxRows; i++) {
              Map<String, Object> wellsInRow = new HashMap<String, Object>();
              for (int j = 1; j <= maxColumns; j++) {
                wellsInRow.put(Integer.toString(j), new HashMap<String, String>());
              }
              plateData.put(Integer.toString(i), wellsInRow);
            }
            ttiPlatesByTest.put(t, plateData);
          }
        }
        else if (rowNum == maxRows) {
          rowNum = 1;
          colNum++;
        }
        
        Map<String, Map<String, Object>> plate = (Map<String, Map<String, Object>>) ttiPlatesByTest.get(testId.toString());

        Map<String, String> wellData = (Map<String, String>) plate.get(rowNum.toString()).get(colNum.toString());
        wellData.put("contents", "sample");
        wellData.put("donationIdentificationNumber", donationIdentificationNumberMap.get(donationId));
        wellData.put("welltype", "1");
        wellData.put("testResult", testResultsForDonation.get(testId));
        wellData.put("machineReading", Double.toString(Math.random()));

        // fill data in the plates in column major manner
        rowNum++;
      }
    }

    for (String t : allBloodTestsIds) {
      bloodTestingRepository.saveTTIResultsOnPlate((Map<String, Map<String, Object>>) ttiPlatesByTest.get(t), Long.parseLong(t));
    }
    // if plates not required then the following line should be used in place of the code above
    // bloodTestingRepository.saveBloodTestingResults(testResults, true);
  }

  public void createRequests(int numRequests) {

    String[] bloodAbos = { "A", "B", "AB", "O"};
    String[] bloodRhs = { "+", "-"};

    List<Location> sites = locationRepository.getAllUsageSites();
    List<ProductType> productTypes = productTypeRepository.getAllProductTypes();
    List<RequestType> requestTypes = requestTypeRepository.getAllRequestTypes();

    List<String> requestNumbers = sequenceNumberRepository.getBatchRequestNumbers(numRequests);

    List<Request> requests = new ArrayList<Request>();
    for (int i = 0; i < numRequests; i++) {
      Date requestDate = getRandomRequestDate();
      Date requiredDate = new DateTime(requestDate).plusDays(random.nextInt(21)).toDate();
      RequestBackingForm form = new RequestBackingForm();
      form.setRequestNumber(requestNumbers.get(i));
      form.setRequestDate(CustomDateFormatter.getDateTimeString(requestDate));
      form.setRequiredDate(CustomDateFormatter.getDateTimeString(requiredDate));
    /** issue - #225 straight object bindings
      form.setProductType(productTypes.get(random.nextInt(productTypes.size())).getId().toString());
      form.setRequestType(requestTypes.get(random.nextInt(requestTypes.size())).getId().toString());
      form.setRequestSite(sites.get(random.nextInt(sites.size())).getId().toString());*/
      form.setNumUnitsRequested(1 + Math.abs(random.nextInt()) % 20);
      form.setIsDeleted(false);
      form.setFulfilled(false);

      String bloodAboStr = bloodAbos[random.nextInt(bloodAbos.length)];
      String bloodRhStr = bloodRhs[random.nextInt(bloodRhs.length)];
      form.setPatientBloodAbo(bloodAboStr);
      form.setPatientBloodRh(bloodRhStr);
      requests.add(form.getRequest());
    }
    requestRepository.addAllRequests(requests);
  }
}

