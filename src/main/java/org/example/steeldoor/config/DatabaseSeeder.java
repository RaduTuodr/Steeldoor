package org.example.steeldoor.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.steeldoor.model.*;
import org.example.steeldoor.model.enums.CompanySize;
import org.example.steeldoor.model.enums.ProblemDifficulty;
import org.example.steeldoor.model.enums.RoundType;
import org.example.steeldoor.model.keys.RoundProblemId;
import org.example.steeldoor.model.keys.RoundQuestionId;
import org.example.steeldoor.model.keys.VoteId;
import org.example.steeldoor.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("DatabaseSeeder: starting seed check...");

        List<Role>     roles     = seedRoles();
        List<User>     users     = seedUsers(roles);
        List<Company>  companies = seedCompanies();
        seedOfficeLocations(companies);
        List<Problem>  problems  = seedProblems();
        List<Question> questions = seedQuestions();
        List<Submission>     submissions = seedSubmissions(users, companies);
        List<InterviewRound> rounds      = seedInterviewRounds(submissions);
        seedRoundProblems(rounds, problems);
        seedRoundQuestions(rounds, questions);
        seedVotes(users, submissions);

        log.info("DatabaseSeeder: seed check complete.");
    }

    private List<Role> seedRoles() {
        long count = count("SELECT COUNT(r) FROM Role r");
        if (count > 0) {
            log.info("Roles already seeded ({} rows), skipping.", count);
            return em.createQuery("SELECT r FROM Role r", Role.class).getResultList();
        }

        Role admin = Role.builder().name("ADMIN").build();
        Role user  = Role.builder().name("MEMBER").build();

        em.persist(admin);
        em.persist(user);
        em.flush();

        log.info("Seeded 2 roles.");
        return List.of(admin, user);
    }

    private List<User> seedUsers(List<Role> roles) {
        if (userRepository.count() > 0) {
            log.info("Users already seeded, skipping.");
            return userRepository.findAll();
        }

        Role adminRole = roleByName(roles, "ADMIN");
        Role userRole  = roleByName(roles, "MEMBER");

        User alice = User.builder()
                .username("alice_dev")
                .email("alice@example.com")
                .passwordHash(passwordEncoder.encode("password"))
                .role(adminRole)
                .createdAt(OffsetDateTime.now())
                .build();

        User bob = User.builder()
                .username("bob_coder")
                .email("bob@example.com")
                .passwordHash(passwordEncoder.encode("password"))
                .role(userRole)
                .createdAt(OffsetDateTime.now().minusDays(5))
                .build();

        User carol = User.builder()
                .username("carol_eng")
                .email("carol@example.com")
                .passwordHash(passwordEncoder.encode("password"))
                .role(userRole)
                .createdAt(OffsetDateTime.now().minusDays(10))
                .build();

        userRepository.saveAll(List.of(alice, bob, carol));
        log.info("Seeded 3 users.");
        return List.of(alice, bob, carol);
    }

    private List<Company> seedCompanies() {
        long count = count("SELECT COUNT(c) FROM Company c");
        if (count > 0) {
            log.info("Companies already seeded ({} rows), skipping.", count);
            return em.createQuery("SELECT c FROM Company c", Company.class).getResultList();
        }

        Company google = Company.builder()
                .name("Google")
                .logoUrl("https://storage.googleapis.com/steeldoor/3ca1c783-1844-46d1-91f3-0b50e31b5c0f-icons8-google-logo-96.png")
                .website("https://careers.google.com")
                .description("Multinational technology company specialising in internet services.")
                .industry("Technology")
                .location("Mountain View, CA")
                .size(CompanySize.ENTERPRISE)
                .build();

        Company amazon = Company.builder()
                .name("Amazon")
                .logoUrl("https://storage.googleapis.com/steeldoor/c31653e3-529e-4cff-8e5d-58e1dbccb82b-e8951206c6ba26ce035d6f312be4ec40.png")
                .website("https://www.amazon.jobs")
                .description("E-commerce and cloud computing giant.")
                .industry("Technology / E-Commerce")
                .location("Seattle, WA")
                .size(CompanySize.ENTERPRISE)
                .build();

        Company stripe = Company.builder()
                .name("Stripe")
                .logoUrl("https://storage.googleapis.com/steeldoor/bedabd1b-32d5-41ad-a230-fc604917e202-Stripe-Emblem.png")
                .website("https://stripe.com/jobs")
                .description("Financial infrastructure platform for the internet.")
                .industry("FinTech")
                .location("San Francisco, CA")
                .size(CompanySize.STARTUP)
                .build();

        em.persist(google);
        em.persist(amazon);
        em.persist(stripe);
        em.flush();

        log.info("Seeded 3 companies.");
        return List.of(google, amazon, stripe);
    }

    private void seedOfficeLocations(List<Company> companies) {
        long count = count("SELECT COUNT(o) FROM OfficeLocation o");
        if (count > 0) {
            log.info("Office locations already seeded ({} rows), skipping.", count);
            return;
        }

        Company google = companies.get(0);
        Company amazon = companies.get(1);
        Company stripe = companies.get(2);

        List<OfficeLocation> locations = List.of(
                OfficeLocation.builder().company(google).city("Mountain View").country("USA").build(),
                OfficeLocation.builder().company(google).city("London").country("UK").build(),
                OfficeLocation.builder().company(amazon).city("Seattle").country("USA").build(),
                OfficeLocation.builder().company(amazon).city("Berlin").country("Germany").build(),
                OfficeLocation.builder().company(stripe).city("San Francisco").country("USA").build(),
                OfficeLocation.builder().company(stripe).city("Dublin").country("Ireland").build()
        );

        locations.forEach(em::persist);
        em.flush();
        log.info("Seeded {} office locations.", locations.size());
    }

    private List<Problem> seedProblems() {
        long count = count("SELECT COUNT(p) FROM Problem p");
        if (count > 0) {
            log.info("Problems already seeded ({} rows), skipping.", count);
            return em.createQuery("SELECT p FROM Problem p", Problem.class).getResultList();
        }

        List<Problem> problems = List.of(
                Problem.builder()
                        .name("Two Sum")
                        .platform("LeetCode")
                        .link("https://leetcode.com/problems/two-sum/")
                        .difficulty(ProblemDifficulty.Easy)
                        .build(),
                Problem.builder()
                        .name("Merge Intervals")
                        .platform("LeetCode")
                        .link("https://leetcode.com/problems/merge-intervals/")
                        .difficulty(ProblemDifficulty.Medium)
                        .build(),
                Problem.builder()
                        .name("LRU Cache")
                        .platform("LeetCode")
                        .link("https://leetcode.com/problems/lru-cache/")
                        .difficulty(ProblemDifficulty.Medium)
                        .build(),
                Problem.builder()
                        .name("Median of Two Sorted Arrays")
                        .platform("LeetCode")
                        .link("https://leetcode.com/problems/median-of-two-sorted-arrays/")
                        .difficulty(ProblemDifficulty.Hard)
                        .build(),
                Problem.builder()
                        .name("Design Twitter")
                        .platform("LeetCode")
                        .link("https://leetcode.com/problems/design-twitter/")
                        .difficulty(ProblemDifficulty.Medium)
                        .build()
        );

        problems.forEach(em::persist);
        em.flush();
        log.info("Seeded {} problems.", problems.size());
        return problems;
    }

    private List<Question> seedQuestions() {
        long count = count("SELECT COUNT(q) FROM Question q");
        if (count > 0) {
            log.info("Questions already seeded ({} rows), skipping.", count);
            return em.createQuery("SELECT q FROM Question q", Question.class).getResultList();
        }

        List<Question> questions = List.of(
                Question.builder()
                        .content("Tell me about a time you had to deal with a difficult team member.")
                        .category("Behavioral")
                        .build(),
                Question.builder()
                        .content("Describe a project where you had to make a significant technical trade-off.")
                        .category("Behavioral")
                        .build(),
                Question.builder()
                        .content("How would you design a URL shortener like bit.ly?")
                        .category("System Design")
                        .build(),
                Question.builder()
                        .content("Explain the difference between a process and a thread.")
                        .category("Technical")
                        .build(),
                Question.builder()
                        .content("What is your greatest professional weakness and how are you addressing it?")
                        .category("HR")
                        .build()
        );

        questions.forEach(em::persist);
        em.flush();
        log.info("Seeded {} questions.", questions.size());
        return questions;
    }

    private List<Submission> seedSubmissions(List<User> users, List<Company> companies) {
        long count = count("SELECT COUNT(s) FROM Submission s");
        if (count > 0) {
            log.info("Submissions already seeded ({} rows), skipping.", count);
            return em.createQuery("SELECT s FROM Submission s", Submission.class).getResultList();
        }

        User alice = users.get(0);
        User bob   = users.get(1);
        User carol = users.get(2);

        Company google = companies.get(0);
        Company amazon = companies.get(1);
        Company stripe = companies.get(2);

        List<Submission> submissions = List.of(
                Submission.builder()
                        .user(alice)
                        .company(google)
                        .position("Software Engineer L4")
                        .overallDifficulty(4)
                        .offerReceived(true)
                        .createdAt(OffsetDateTime.now().minusDays(30))
                        .build(),
                Submission.builder()
                        .user(bob)
                        .company(amazon)
                        .position("SDE II")
                        .overallDifficulty(3)
                        .offerReceived(false)
                        .createdAt(OffsetDateTime.now().minusDays(14))
                        .build(),
                Submission.builder()
                        .user(carol)
                        .company(stripe)
                        .position("Backend Engineer")
                        .overallDifficulty(5)
                        .offerReceived(true)
                        .createdAt(OffsetDateTime.now().minusDays(7))
                        .build()
        );

        submissions.forEach(em::persist);
        em.flush();
        log.info("Seeded {} submissions.", submissions.size());
        return submissions;
    }

    private List<InterviewRound> seedInterviewRounds(List<Submission> submissions) {
        long count = count("SELECT COUNT(r) FROM InterviewRound r");
        if (count > 0) {
            log.info("Interview rounds already seeded ({} rows), skipping.", count);
            return em.createQuery("SELECT r FROM InterviewRound r", InterviewRound.class).getResultList();
        }

        Submission googleSub = submissions.get(0);
        Submission amazonSub = submissions.get(1);
        Submission stripeSub = submissions.get(2);

        List<InterviewRound> rounds = List.of(
                // Google: OA -> Technical -> System Design
                InterviewRound.builder()
                        .submission(googleSub).type(RoundType.OA)
                        .title("Online Assessment").durationMinutes(90)
                        .difficulty(3).orderIndex(1).build(),
                InterviewRound.builder()
                        .submission(googleSub).type(RoundType.LIVE_CODING)
                        .title("Technical Phone Screen").durationMinutes(60)
                        .difficulty(4).orderIndex(2).build(),
                InterviewRound.builder()
                        .submission(googleSub).type(RoundType.SYSTEM_DESIGN)
                        .title("System Design Onsite").durationMinutes(60)
                        .difficulty(5).orderIndex(3).build(),

                // Amazon: OA -> Behavioral -> Technical
                InterviewRound.builder()
                        .submission(amazonSub).type(RoundType.OA)
                        .title("Online Assessment").durationMinutes(120)
                        .difficulty(2).orderIndex(1).build(),
                InterviewRound.builder()
                        .submission(amazonSub).type(RoundType.BEHAVIORAL)
                        .title("Leadership Principles").durationMinutes(45)
                        .difficulty(2).orderIndex(2).build(),
                InterviewRound.builder()
                        .submission(amazonSub).type(RoundType.LIVE_CODING)
                        .title("Technical Interview").durationMinutes(60)
                        .difficulty(3).orderIndex(3).build(),

                // Stripe: Technical Discussion -> Live Coding -> HR
                InterviewRound.builder()
                        .submission(stripeSub).type(RoundType.TECHNICAL_DISCUSSION)
                        .title("Architecture Discussion").durationMinutes(60)
                        .difficulty(4).orderIndex(1).build(),
                InterviewRound.builder()
                        .submission(stripeSub).type(RoundType.LIVE_CODING)
                        .title("Pair Programming").durationMinutes(90)
                        .difficulty(5).orderIndex(2).build(),
                InterviewRound.builder()
                        .submission(stripeSub).type(RoundType.HR)
                        .title("HR & Compensation").durationMinutes(30)
                        .difficulty(1).orderIndex(3).build()
        );

        rounds.forEach(em::persist);
        em.flush();
        log.info("Seeded {} interview rounds.", rounds.size());
        return rounds;
    }

    private void seedRoundProblems(List<InterviewRound> rounds, List<Problem> problems) {
        long count = count("SELECT COUNT(rp) FROM RoundProblem rp");
        if (count > 0) {
            log.info("Round-problems already seeded ({} rows), skipping.", count);
            return;
        }

        // Google OA (index 0) → Two Sum, Merge Intervals
        // Google Live Coding (index 1) → LRU Cache
        // Amazon OA (index 3) → Two Sum, Median of Two Sorted Arrays
        // Stripe Live Coding (index 7) → Design Twitter
        List<RoundProblem> roundProblems = List.of(
                roundProblem(rounds.get(0), problems.get(0)),
                roundProblem(rounds.get(0), problems.get(1)),
                roundProblem(rounds.get(1), problems.get(2)),
                roundProblem(rounds.get(3), problems.get(0)),
                roundProblem(rounds.get(3), problems.get(3)),
                roundProblem(rounds.get(7), problems.get(4))
        );

        roundProblems.forEach(em::persist);
        em.flush();
        log.info("Seeded {} round-problem links.", roundProblems.size());
    }

    private void seedRoundQuestions(List<InterviewRound> rounds, List<Question> questions) {
        long count = count("SELECT COUNT(rq) FROM RoundQuestion rq");
        if (count > 0) {
            log.info("Round-questions already seeded ({} rows), skipping.", count);
            return;
        }

        // Google System Design (index 2) → URL shortener question
        // Amazon Behavioral (index 4) → Difficult team member, trade-off
        // Stripe Technical Discussion (index 6) → Process vs thread
        // Stripe HR (index 8) → Greatest weakness
        List<RoundQuestion> roundQuestions = List.of(
                roundQuestion(rounds.get(2), questions.get(2)),
                roundQuestion(rounds.get(4), questions.get(0)),
                roundQuestion(rounds.get(4), questions.get(1)),
                roundQuestion(rounds.get(6), questions.get(3)),
                roundQuestion(rounds.get(8), questions.get(4))
        );

        roundQuestions.forEach(em::persist);
        em.flush();
        log.info("Seeded {} round-question links.", roundQuestions.size());
    }

    private void seedVotes(List<User> users, List<Submission> submissions) {
        long count = count("SELECT COUNT(v) FROM Vote v");
        if (count > 0) {
            log.info("Votes already seeded ({} rows), skipping.", count);
            return;
        }

        User alice = users.get(0);
        User bob   = users.get(1);
        User carol = users.get(2);

        Submission googleSub = submissions.get(0);
        Submission amazonSub = submissions.get(1);
        Submission stripeSub = submissions.get(2);

        // Users vote on each other's submissions (not their own)
        List<Vote> votes = List.of(
                vote(bob,   googleSub),   // bob upvotes alice's Google submission
                vote(carol, googleSub),   // carol upvotes alice's Google submission
                vote(alice, amazonSub),   // alice downvotes bob's Amazon submission
                vote(carol, amazonSub),   // carol upvotes bob's Amazon submission
                vote(alice, stripeSub),   // alice upvotes carol's Stripe submission
                vote(bob,   stripeSub)    // bob upvotes carol's Stripe submission
        );

        votes.forEach(em::persist);
        em.flush();
        log.info("Seeded {} votes.", votes.size());
    }

    private long count(String jpql) {
        return em.createQuery(jpql, Long.class).getSingleResult();
    }

    private Role roleByName(List<Role> roles, String name) {
        return roles.stream()
                .filter(r -> r.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Role not found: " + name));
    }

    private RoundProblem roundProblem(InterviewRound round, Problem problem) {
        RoundProblemId id = new RoundProblemId(round.getId(), problem.getId());
        return RoundProblem.builder().id(id).round(round).problem(problem).build();
    }

    private RoundQuestion roundQuestion(InterviewRound round, Question question) {
        RoundQuestionId id = new RoundQuestionId(round.getId(), question.getId());
        return RoundQuestion.builder().id(id).round(round).question(question).build();
    }

    private Vote vote(User user, Submission submission) {
        VoteId id = new VoteId(user.getId(), submission.getId());
        return Vote.builder().id(id).user(user).submission(submission).build();
    }
}