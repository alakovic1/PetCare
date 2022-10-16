package ba.unsa.etf.nwt.user_service.seeder;

import ba.unsa.etf.nwt.user_service.model.Answer;
import ba.unsa.etf.nwt.user_service.model.Question;
import ba.unsa.etf.nwt.user_service.model.User;
import ba.unsa.etf.nwt.user_service.model.roles.Role;
import ba.unsa.etf.nwt.user_service.model.roles.RoleName;
import ba.unsa.etf.nwt.user_service.service.AnswerService;
import ba.unsa.etf.nwt.user_service.service.QuestionService;
import ba.unsa.etf.nwt.user_service.service.RoleService;
import ba.unsa.etf.nwt.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseSeeder {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedDatabase();
    }

    private void seedDatabase() {
        //unos rola.. (samo jednom)
        Role r1 = createRole(RoleName.ROLE_ADMIN);
        Role r2 = createRole(RoleName.ROLE_USER);
        Set<Role> r = new HashSet<>();
        Set<Role> roles = new HashSet<>();
        r.add(r1);
        roles.add(r2);

        //unos pitanja
        Question q1 = createQuestion("What is the name of the town where you were born?", "Home town");
        Question q2 = createQuestion("What elementary school did you attend?", "First school");
        Question q3 = createQuestion("What was your first car?", "Car details");
        Question q4 = createQuestion("What is the name of your first pet?", "Pet details");
        Question q5 = createQuestion("What is your mother's maiden name?", "Mother information");
        Question q6 = createQuestion("Who was your childhood hero?", "Childhood hero memory");

        //prvi user - admin
        Answer a1 = createAnswer("Sarajevo", q1);
        createUser("Amila", "Lakovic", "alakovic1@etf.unsa.ba", "alakovic1", "Password123!", a1, r);

        //drugi user - admin
        Answer a2 = createAnswer("Passat", q3);
        createUser("Samra", "Mujcinovic", "smujcinovi1@etf.unsa.ba", "smujcinovi1", "Password234?", a2, r);

        //treci user - admin
        Answer a3 = createAnswer("Spider-Man", q6);
        createUser("Emir", "Pita", "epita1@etf.unsa.ba", "epita1", "PASSword3!", a3, r);

        //cetvrti user - admin
        Answer a4 = createAnswer("Zenica", q1);
        createUser("Amila", "Hrustic", "ahrustic2@etf.unsa.ba", "ahrustic2", "PASSWORDDd4?", a4, r);

        //peti user - user
        Answer a5 = createAnswer("Ella", q4);
        createUser("Name", "Surname", "email@etf.unsa.ba", "username", "string123!A", a5, roles);

        //sesti user - admin
        Answer a6 = createAnswer("Sarajevo", q1);
        createUser("Irfan", "Prazina", "iprazina1@etf.unsa.ba", "iprazina1", "NWTpassword123!", a6, r);
    }

    private void createUser(String name, String surname, String email, String username, String password, Answer answer, Set<Role> roles) {
        User u = new User();
        u.setName(name);
        u.setSurname(surname);
        u.setEmail(email);
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setAnswer(answer);
        u.setRoles(roles);
        userService.save(u);
    }

    private Question createQuestion(String title, String description) {
        Question q = new Question();
        q.setTitle(title);
        q.setDescription(description);
        questionService.save(q);
        return q;
    }

    private Answer createAnswer(String text, Question question) {
        Answer a = new Answer();
        a.setText(text);
        a.setQuestion(question);
        answerService.save(a);
        return a;
    }

    private Role createRole(RoleName roleName) {
        Role r = new Role(roleName);
        roleService.save(r);
        return r;
    }

}
