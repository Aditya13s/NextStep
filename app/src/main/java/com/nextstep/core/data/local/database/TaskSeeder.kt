package com.nextstep.core.data.local.database

import com.nextstep.core.data.local.entity.TaskEntity

/**
 * TaskSeeder.kt
 * ─────────────────────────────────────────────────────────
 * Provides the pre-defined 30-day task library for each goal.
 *
 * WHY pre-defined tasks?
 *   The product spec says "tasks are pre-defined" and each
 *   task must be < 30 minutes, clear, and actionable.  Rather
 *   than fetching from a server (requires internet + backend),
 *   we ship the full library inside the app and seed Room on
 *   first launch.  This makes the app 100% offline-capable.
 *
 * HOW seeding works:
 *   1. The app launches → DatabaseModule provides the DB.
 *   2. AppModule provides TaskSeeder as a @Singleton.
 *   3. NextStepApp.onCreate() calls TaskSeeder.seedIfNeeded()
 *      in a coroutine on IO dispatcher.
 *   4. seedIfNeeded() checks TaskDao.countTasksForCategory()
 *      for each goal; skips goals that already have rows.
 *   5. On subsequent launches the count > 0 check prevents
 *      duplicate inserts.
 *
 * ADDING MORE TASKS:
 *   1. Add rows to the relevant tasksFor…() function.
 *   2. Bump Database version and write a Migration to insert
 *      the new rows into existing installations.
 * ─────────────────────────────────────────────────────────
 */
object TaskSeeder {

    /**
     * Returns ALL pre-defined tasks across all goals.
     * TaskRepositoryImpl calls this once during seeding.
     */
    fun allTasks(): List<TaskEntity> =
        tasksForFitness() +
        tasksForStudy() +
        tasksForProductivity() +
        tasksForMentalHealth() +
        tasksForCoding()

    // ── ID ranges ─────────────────────────────────────────
    // Each goal gets a dedicated ID block so IDs never collide:
    //   Fitness:       1 – 30
    //   Study:        31 – 60
    //   Productivity: 61 – 90
    //   Mental Health:91 – 120
    //   Coding:      121 – 150

    // ─────────────────────────────────────────────────────
    // 🏋️  FITNESS  (Days 1-30, IDs 1-30)
    // ─────────────────────────────────────────────────────
    private fun tasksForFitness(): List<TaskEntity> = listOf(
        TaskEntity(1,  "Fitness", "Fitness", 1,  "Do 10 push-ups", "Start small. Drop and do 10 push-ups right now. Form > speed. Keep your body straight.", 5),
        TaskEntity(2,  "Fitness", "Fitness", 2,  "Walk for 15 minutes", "Go outside and walk at a comfortable pace. No phone – just breathe and move.", 15),
        TaskEntity(3,  "Fitness", "Fitness", 3,  "Do 15 squats", "Stand with feet shoulder-width apart. Lower until thighs are parallel to the floor. 3 sets of 5.", 10),
        TaskEntity(4,  "Fitness", "Fitness", 4,  "Stretch for 10 minutes", "Focus on hamstrings, quads, and shoulders. Hold each stretch for 30 seconds. No bouncing.", 10),
        TaskEntity(5,  "Fitness", "Fitness", 5,  "Do 20 jumping jacks", "A quick cardio burst. Keep your core tight and land softly. 2 sets of 10.", 5),
        TaskEntity(6,  "Fitness", "Fitness", 6,  "Hold a plank for 30 seconds", "Elbows under shoulders, body in a straight line. Squeeze your core the whole time.", 5),
        TaskEntity(7,  "Fitness", "Fitness", 7,  "Do 12 lunges (each leg)", "Step forward, lower your back knee to just above the floor. Alternate legs.", 10),
        TaskEntity(8,  "Fitness", "Fitness", 8,  "Do 15 push-ups", "You did 10 on day 1 – today add 5 more. Progress is the goal.", 8),
        TaskEntity(9,  "Fitness", "Fitness", 9,  "Walk 20 minutes", "Slightly longer than day 2. Pick a new route to keep it interesting.", 20),
        TaskEntity(10, "Fitness", "Fitness", 10, "Do a 5-minute warm-up + 20 squats", "Arm circles, leg swings, then 20 squats. Your legs should burn by the last rep.", 12),
        TaskEntity(11, "Fitness", "Fitness", 11, "Do 3 sets of 10 push-ups", "Rest 60 seconds between sets. Focus on full range of motion.", 15),
        TaskEntity(12, "Fitness", "Fitness", 12, "Hold a plank for 45 seconds", "15 more seconds than day 6. Breathe steadily – don't hold your breath.", 5),
        TaskEntity(13, "Fitness", "Fitness", 13, "Do 20 calf raises", "Stand on the edge of a step. Rise up on your toes slowly, lower slowly.", 8),
        TaskEntity(14, "Fitness", "Fitness", 14, "Walk 25 minutes", "Week 2 cardio. Listen to a podcast or music to make it enjoyable.", 25),
        TaskEntity(15, "Fitness", "Fitness", 15, "Do 15 dips (using a chair)", "Hands on chair edge, feet out, lower until elbows are 90°. Great for triceps.", 10),
        TaskEntity(16, "Fitness", "Fitness", 16, "Full-body stretch – 15 minutes", "Neck, shoulders, back, hips, hamstrings. Slow and controlled.", 15),
        TaskEntity(17, "Fitness", "Fitness", 17, "Do 25 squats", "You've been building up. 25 feels hard but you can do it.", 10),
        TaskEntity(18, "Fitness", "Fitness", 18, "Do 20 push-ups", "Half-way through your fitness journey. Celebrate with a strong set.", 10),
        TaskEntity(19, "Fitness", "Fitness", 19, "Mountain climbers – 3 sets of 15", "From plank position, drive knees to chest alternately at a steady pace.", 12),
        TaskEntity(20, "Fitness", "Fitness", 20, "Walk 30 minutes", "Full 30-minute walk. You've earned a longer reward walk.", 30),
        TaskEntity(21, "Fitness", "Fitness", 21, "Do 4 sets of 10 push-ups", "Four sets today. Rest 60s between each.", 18),
        TaskEntity(22, "Fitness", "Fitness", 22, "Plank + side planks (30s each)", "Centre plank, then left side, then right side. Core stability.", 10),
        TaskEntity(23, "Fitness", "Fitness", 23, "Do 30 squats", "Final squat milestone. Go slow on the way down.", 12),
        TaskEntity(24, "Fitness", "Fitness", 24, "Do 20 lunges + 20 calf raises", "Combine two exercises for a legs day.", 15),
        TaskEntity(25, "Fitness", "Fitness", 25, "Burpees – 3 sets of 5", "Squat-thrust-jump combo. Rest 90s between sets. Great full-body exercise.", 15),
        TaskEntity(26, "Fitness", "Fitness", 26, "Walk 30 mins + 20 push-ups", "Cardio + strength in one session.", 35),
        TaskEntity(27, "Fitness", "Fitness", 27, "Plank 1 minute", "The goal was 30 seconds on day 6. Today: 60 seconds straight.", 5),
        TaskEntity(28, "Fitness", "Fitness", 28, "5 sets of 10 push-ups", "Consistency built this. Five sets is impressive.", 20),
        TaskEntity(29, "Fitness", "Fitness", 29, "Full-body circuit: squats+push-ups+lunges+plank", "3 rounds: 15 squats, 10 push-ups, 10 lunges, 30s plank.", 25),
        TaskEntity(30, "Fitness", "Fitness", 30, "Celebrate: 30-min walk + journaling", "You finished 30 days! Walk and reflect on how far you've come. Write 3 things you're proud of.", 30)
    )

    // ─────────────────────────────────────────────────────
    // 📚  STUDY  (Days 1-30, IDs 31-60)
    // ─────────────────────────────────────────────────────
    private fun tasksForStudy(): List<TaskEntity> = listOf(
        TaskEntity(31, "Study", "Study", 1,  "Read for 15 minutes", "Pick any book or article. Set a timer. Just read – no multitasking.", 15),
        TaskEntity(32, "Study", "Study", 2,  "Write a 5-sentence summary", "Summarise what you read yesterday in 5 sentences. Writing solidifies memory.", 10),
        TaskEntity(33, "Study", "Study", 3,  "Learn one new word", "Pick an unfamiliar word, learn its meaning and etymology. Use it in a sentence.", 10),
        TaskEntity(34, "Study", "Study", 4,  "Watch a 10-min educational video", "YouTube, Khan Academy, or Coursera. Topic must be outside your usual comfort zone.", 10),
        TaskEntity(35, "Study", "Study", 5,  "Make 5 flash cards", "Write a concept on the front, explanation on the back. Use them for revision later.", 15),
        TaskEntity(36, "Study", "Study", 6,  "Teach someone one concept", "Explain something you learned this week to a friend, family member, or even yourself aloud.", 15),
        TaskEntity(37, "Study", "Study", 7,  "Read for 20 minutes", "Slightly longer session. Try to enter a focused flow state.", 20),
        TaskEntity(38, "Study", "Study", 8,  "Solve 3 practice problems", "From any subject you're learning. Retrieve from memory before checking answers.", 20),
        TaskEntity(39, "Study", "Study", 9,  "Organise your notes", "Review last week's notes, highlight key points and remove redundant content.", 20),
        TaskEntity(40, "Study", "Study", 10, "Write a 1-page essay on a topic", "Pick any topic and write 1 page. The act of writing reveals gaps in understanding.", 25),
        TaskEntity(41, "Study", "Study", 11, "Learn about a historical event", "Read a short article about an event you know little about. Note 3 interesting facts.", 15),
        TaskEntity(42, "Study", "Study", 12, "Practice mental math for 10 min", "Estimate, calculate, and verify. Apps like Lumosity or pen and paper work well.", 10),
        TaskEntity(43, "Study", "Study", 13, "Read for 25 minutes", "Increase reading stamina. Go deeper into your current subject.", 25),
        TaskEntity(44, "Study", "Study", 14, "Create a mind map", "Draw a central topic and branch out with related subtopics and connections.", 20),
        TaskEntity(45, "Study", "Study", 15, "Review your flash cards", "Go through the 5+ cards you've made. Rate yourself on each. Re-do missed ones.", 15),
        TaskEntity(46, "Study", "Study", 16, "Listen to an educational podcast", "30 minutes while commuting, exercising, or doing chores.", 30),
        TaskEntity(47, "Study", "Study", 17, "Write down 3 things you learned this week", "Weekly reflection. Retrieval practice strengthens long-term memory.", 10),
        TaskEntity(48, "Study", "Study", 18, "Solve 5 practice problems", "Two more than day 8. Push for speed AND accuracy.", 25),
        TaskEntity(49, "Study", "Study", 19, "Explore a new subject for 15 min", "Curiosity is the engine of learning. Pick something you've always been curious about.", 15),
        TaskEntity(50, "Study", "Study", 20, "Read for 30 minutes", "Full 30-minute deep-reading session. No phone nearby.", 30),
        TaskEntity(51, "Study", "Study", 21, "Make 10 flash cards", "Double the set from day 5. Great variety of subjects.", 20),
        TaskEntity(52, "Study", "Study", 22, "Teach a concept via voice note", "Record a 3-minute voice memo explaining a concept as if teaching a beginner.", 15),
        TaskEntity(53, "Study", "Study", 23, "Summarise a chapter or article", "1-paragraph summary: main idea, 3 key points, your opinion.", 15),
        TaskEntity(54, "Study", "Study", 24, "Solve a puzzle or logic problem", "Sudoku, chess puzzle, or a logic riddle. Pattern recognition is a core study skill.", 20),
        TaskEntity(55, "Study", "Study", 25, "Identify and fill one knowledge gap", "What is something you think you understand but actually don't? Spend 20 min on it.", 20),
        TaskEntity(56, "Study", "Study", 26, "Read for 30 min + take notes", "Active reading: summarise each page in one sentence as you go.", 30),
        TaskEntity(57, "Study", "Study", 27, "Review all your flash cards", "Full review of everything you've made. Spaced repetition at work.", 20),
        TaskEntity(58, "Study", "Study", 28, "Write a 2-page essay", "Longer essay – structure: introduction, 3 main points, conclusion.", 30),
        TaskEntity(59, "Study", "Study", 29, "Create a study plan for next month", "List 4 subjects, set weekly goals, schedule 30 min sessions.", 20),
        TaskEntity(60, "Study", "Study", 30, "Share your top 3 learnings", "Write a post, message, or journal entry: what are the 3 most valuable things you learned this month?", 15)
    )

    // ─────────────────────────────────────────────────────
    // ✅  PRODUCTIVITY  (Days 1-30, IDs 61-90)
    // ─────────────────────────────────────────────────────
    private fun tasksForProductivity(): List<TaskEntity> = listOf(
        TaskEntity(61, "Productivity", "Productivity", 1,  "Write tomorrow's top 3 tasks", "Tonight, write the 3 most important things you'll do tomorrow. No more than 3.", 5),
        TaskEntity(62, "Productivity", "Productivity", 2,  "Clear your physical workspace", "Remove everything not needed for today's work. A clean desk = a clear mind.", 10),
        TaskEntity(63, "Productivity", "Productivity", 3,  "Do a 25-min Pomodoro session", "Set a timer for 25 minutes. Work on ONE task only. Zero distractions.", 25),
        TaskEntity(64, "Productivity", "Productivity", 4,  "Delete 20 unnecessary emails/files", "Digital clutter creates decision fatigue. Clear it.", 15),
        TaskEntity(65, "Productivity", "Productivity", 5,  "Identify your biggest time waster", "Be honest. Write down the #1 thing that steals your focus. Then write a fix.", 10),
        TaskEntity(66, "Productivity", "Productivity", 6,  "Do a 2-minute rule pass", "Scan your to-do list. Do every item that takes < 2 minutes RIGHT NOW.", 15),
        TaskEntity(67, "Productivity", "Productivity", 7,  "Plan your ideal week", "Block time for deep work, exercise, rest, and family. Use paper or a calendar app.", 20),
        TaskEntity(68, "Productivity", "Productivity", 8,  "Complete a task you've been avoiding", "You know the one. Set a timer for 30 minutes and start.", 30),
        TaskEntity(69, "Productivity", "Productivity", 9,  "Turn off all notifications for 1 hour", "Go into Focus mode or DND. Notice how much more you get done.", 60),
        TaskEntity(70, "Productivity", "Productivity", 10, "Conduct a weekly review", "List what you completed, what's pending, and what you'll do next week.", 20),
        TaskEntity(71, "Productivity", "Productivity", 11, "Set up a morning routine (write it down)", "3–5 actions that start your day with intention. E.g. water, stretch, priorities.", 10),
        TaskEntity(72, "Productivity", "Productivity", 12, "Batch similar tasks together", "Group emails, calls, or errands. Context-switching wastes 20 min per switch.", 20),
        TaskEntity(73, "Productivity", "Productivity", 13, "Do 3 Pomodoro sessions", "75 minutes of real focused work. Rest 5 min between each session.", 90),
        TaskEntity(74, "Productivity", "Productivity", 14, "Learn one keyboard shortcut", "Pick the shortcut you'd benefit from most. Practice it 10 times right now.", 5),
        TaskEntity(75, "Productivity", "Productivity", 15, "Automate one repetitive task", "Can you script, template, or schedule something you do manually every day?", 20),
        TaskEntity(76, "Productivity", "Productivity", 16, "Write a NOT-to-do list", "List 5 habits that drain your productivity. Commit to avoiding them this week.", 10),
        TaskEntity(77, "Productivity", "Productivity", 17, "Do a brain dump", "Set a 10-min timer and write every task, worry, and idea in your head onto paper.", 10),
        TaskEntity(78, "Productivity", "Productivity", 18, "Organise your task list by priority", "Re-order your to-dos using the Eisenhower matrix: urgent/important vs not.", 15),
        TaskEntity(79, "Productivity", "Productivity", 19, "Complete your #1 priority before lunch", "Protect your peak-energy hours. Do the hardest thing first.", 30),
        TaskEntity(80, "Productivity", "Productivity", 20, "Set a personal deadline 24h early", "Pick a real deadline and move it 24 hours earlier in your calendar.", 5),
        TaskEntity(81, "Productivity", "Productivity", 21, "Do 4 Pomodoro sessions", "100 minutes of focused work. The most productive day of the month so far.", 100),
        TaskEntity(82, "Productivity", "Productivity", 22, "Unsubscribe from 10 email lists", "Spending 5 minutes now saves hours over the next year.", 10),
        TaskEntity(83, "Productivity", "Productivity", 23, "Review your goals and update them", "Are your goals still relevant? Adjust timelines or targets where needed.", 15),
        TaskEntity(84, "Productivity", "Productivity", 24, "Say no to one low-value commitment", "Protecting your time is a skill. Decline one thing today that doesn't serve you.", 5),
        TaskEntity(85, "Productivity", "Productivity", 25, "Create a personal SOP for a regular task", "Document a process you do regularly (e.g. morning email triage) as a step-by-step guide.", 20),
        TaskEntity(86, "Productivity", "Productivity", 26, "Do a full digital declutter", "Clear downloads, desktop, and screenshots folders. Archive old files.", 20),
        TaskEntity(87, "Productivity", "Productivity", 27, "Time-block tomorrow in detail", "Assign specific tasks to specific time slots. Leave buffer time.", 15),
        TaskEntity(88, "Productivity", "Productivity", 28, "Complete 5 Pomodoro sessions", "125 minutes of focused work. This is your personal best so far.", 125),
        TaskEntity(89, "Productivity", "Productivity", 29, "Review this month's productivity wins", "List 5 things you did better this month vs last month. Celebrate specifics.", 15),
        TaskEntity(90, "Productivity", "Productivity", 30, "Write your productivity system", "Document your personal system: planning, focus, review. 1 page only.", 20)
    )

    // ─────────────────────────────────────────────────────
    // 🧘  MENTAL HEALTH  (Days 1-30, IDs 91-120)
    // ─────────────────────────────────────────────────────
    private fun tasksForMentalHealth(): List<TaskEntity> = listOf(
        TaskEntity(91,  "Mental Health", "Mental Health", 1,  "Write 3 things you're grateful for", "Gratitude rewires the brain toward positivity. Be specific – not just 'health'.", 5),
        TaskEntity(92,  "Mental Health", "Mental Health", 2,  "Take a 10-minute mindful walk", "Walk slowly. Notice 5 things you can see, 4 you can touch, 3 you can hear.", 10),
        TaskEntity(93,  "Mental Health", "Mental Health", 3,  "Do 5 minutes of deep breathing", "4-7-8 technique: inhale 4s, hold 7s, exhale 8s. Repeat 4 times.", 5),
        TaskEntity(94,  "Mental Health", "Mental Health", 4,  "Write about one thing you're worried about", "Get the worry OUT of your head and onto paper. Then write: 'I can handle this.'", 10),
        TaskEntity(95,  "Mental Health", "Mental Health", 5,  "Call or text someone you care about", "Human connection is the single biggest predictor of wellbeing. Reach out.", 10),
        TaskEntity(96,  "Mental Health", "Mental Health", 6,  "Do a digital detox for 1 hour", "No social media, no news, no phone. Read, sit, or take a walk.", 60),
        TaskEntity(97,  "Mental Health", "Mental Health", 7,  "Write a letter to your future self", "What do you want your life to look like in 1 year? What would you tell yourself?", 20),
        TaskEntity(98,  "Mental Health", "Mental Health", 8,  "Practice progressive muscle relaxation", "Tense each muscle group for 5s, release. Start from toes, work up to face.", 15),
        TaskEntity(99,  "Mental Health", "Mental Health", 9,  "Identify one negative thought pattern", "Notice self-critical thoughts. Write them down and challenge each one.", 15),
        TaskEntity(100, "Mental Health", "Mental Health", 10, "Do something creative for 20 minutes", "Draw, write, cook, play music – anything that gets you into a flow state.", 20),
        TaskEntity(101, "Mental Health", "Mental Health", 11, "Meditate for 5 minutes", "Sit quietly, focus on your breath. When your mind wanders, gently return. Use Headspace or Insight Timer.", 5),
        TaskEntity(102, "Mental Health", "Mental Health", 12, "Journal about a positive memory", "Describe a happy memory in detail. Reliving joy activates the same neural pathways.", 15),
        TaskEntity(103, "Mental Health", "Mental Health", 13, "Set one healthy boundary today", "Say no to something that drains you, or ask for help with something that overwhelms you.", 10),
        TaskEntity(104, "Mental Health", "Mental Health", 14, "Take a tech-free meal", "Eat one meal today without a screen. Focus on taste, texture, and being present.", 20),
        TaskEntity(105, "Mental Health", "Mental Health", 15, "List 10 things that make you happy", "Simple things count: coffee, sunsets, dogs. Reconnect with what actually brings you joy.", 10),
        TaskEntity(106, "Mental Health", "Mental Health", 16, "Write about your values", "What are your top 3 core values? Are your daily actions aligned with them?", 15),
        TaskEntity(107, "Mental Health", "Mental Health", 17, "Do 10 minutes of stretching or yoga", "Gentle movement releases stored physical tension which often mirrors emotional tension.", 10),
        TaskEntity(108, "Mental Health", "Mental Health", 18, "Forgive yourself for one mistake", "Write down something you're holding against yourself. Then write: 'I forgive myself for this.'", 10),
        TaskEntity(109, "Mental Health", "Mental Health", 19, "Spend 20 min in nature", "Park, garden, anywhere with trees. Nature measurably reduces cortisol levels.", 20),
        TaskEntity(110, "Mental Health", "Mental Health", 20, "Practice a random act of kindness", "Buy a stranger a coffee, compliment someone, or help without being asked.", 15),
        TaskEntity(111, "Mental Health", "Mental Health", 21, "Meditate for 10 minutes", "Double the day-11 session. Focus on body sensations, not thoughts.", 10),
        TaskEntity(112, "Mental Health", "Mental Health", 22, "Write down your achievements this month", "List every win, big and small. You've done more than you realise.", 15),
        TaskEntity(113, "Mental Health", "Mental Health", 23, "Identify one self-care gap", "Sleep, nutrition, exercise, social connection, alone time – which is lacking?", 10),
        TaskEntity(114, "Mental Health", "Mental Health", 24, "Read something uplifting for 15 min", "A book, essay, or story that makes you feel hopeful or inspired.", 15),
        TaskEntity(115, "Mental Health", "Mental Health", 25, "Write a self-compassion letter", "Write to yourself as you would to a close friend going through the same struggle.", 20),
        TaskEntity(116, "Mental Health", "Mental Health", 26, "Spend 1 hour doing something you love", "No productivity, no guilt. Pure enjoyment.", 60),
        TaskEntity(117, "Mental Health", "Mental Health", 27, "Meditate for 15 minutes", "Your longest session yet. Use a guided meditation or sit in silence.", 15),
        TaskEntity(118, "Mental Health", "Mental Health", 28, "Write: What would I do if I wasn't afraid?", "Honest reflection. Fear is often what stands between us and the life we want.", 15),
        TaskEntity(119, "Mental Health", "Mental Health", 29, "Plan one intentional self-care day", "Schedule a full day next week dedicated to your wellbeing. Block it in your calendar.", 10),
        TaskEntity(120, "Mental Health", "Mental Health", 30, "Write a letter of gratitude to yourself", "Thank yourself for showing up every day this month. You did the work.", 15)
    )

    // ─────────────────────────────────────────────────────
    // 💻  CODING  (Days 1-30, IDs 121-150)
    // ─────────────────────────────────────────────────────
    private fun tasksForCoding(): List<TaskEntity> = listOf(
        TaskEntity(121, "Coding", "Coding", 1,  "Write a Hello World in a new language", "Pick any language you haven't used. Install it and print 'Hello, World!'", 15),
        TaskEntity(122, "Coding", "Coding", 2,  "Solve an Easy LeetCode problem", "Go to leetcode.com, filter by Easy, and solve one. Read the solution after.", 25),
        TaskEntity(123, "Coding", "Coding", 3,  "Learn about variables and data types", "Study how your chosen language handles int, string, bool, and array.", 20),
        TaskEntity(124, "Coding", "Coding", 4,  "Write a function that adds two numbers", "Simple function creation. Practice function signature, parameters, and return values.", 15),
        TaskEntity(125, "Coding", "Coding", 5,  "Learn about loops", "Write a for loop and a while loop that each print numbers 1 to 10.", 20),
        TaskEntity(126, "Coding", "Coding", 6,  "Solve another Easy LeetCode problem", "You solved one. Now solve a different Easy problem, this time without hints.", 25),
        TaskEntity(127, "Coding", "Coding", 7,  "Learn about arrays and lists", "Create an array, add items, remove items, and loop through it.", 20),
        TaskEntity(128, "Coding", "Coding", 8,  "Build a simple calculator (CLI)", "Add, subtract, multiply, divide. Accept user input from the command line.", 25),
        TaskEntity(129, "Coding", "Coding", 9,  "Learn about conditionals", "Write if/else if/else statements. Build a simple grade classifier.", 20),
        TaskEntity(130, "Coding", "Coding", 10, "Read about Big O notation", "Why is O(n) faster than O(n²)? Read one article + watch one YouTube video.", 20),
        TaskEntity(131, "Coding", "Coding", 11, "Solve a Medium LeetCode problem", "Step up from Easy. Take 30 minutes max, then study the optimal solution.", 30),
        TaskEntity(132, "Coding", "Coding", 12, "Learn about hashmaps/dictionaries", "Create one, add key-value pairs, look up values, iterate over it.", 20),
        TaskEntity(133, "Coding", "Coding", 13, "Build a number guessing game", "App generates a random number 1-100, user has 7 guesses. Give hints.", 25),
        TaskEntity(134, "Coding", "Coding", 14, "Learn about recursion", "Write a recursive function for factorial. Understand base case vs recursive case.", 20),
        TaskEntity(135, "Coding", "Coding", 15, "Solve two Easy problems in 25 min", "Speed practice. Two problems, 12.5 minutes each. This builds interview stamina.", 25),
        TaskEntity(136, "Coding", "Coding", 16, "Learn about classes and OOP basics", "Create a class with properties and methods. Instantiate it and call a method.", 25),
        TaskEntity(137, "Coding", "Coding", 17, "Build a to-do list CLI app", "Add, list, and remove tasks. Persist to a file so tasks survive app restart.", 30),
        TaskEntity(138, "Coding", "Coding", 18, "Learn about error handling", "Implement try/catch/finally. Create a function that throws an exception.", 15),
        TaskEntity(139, "Coding", "Coding", 19, "Solve a Medium LeetCode problem", "Second Medium problem. Track your thought process before coding.", 30),
        TaskEntity(140, "Coding", "Coding", 20, "Read about Git and version control", "Learn: init, add, commit, push, pull, branch, merge. Practice with a new repo.", 25),
        TaskEntity(141, "Coding", "Coding", 21, "Refactor your calculator from day 8", "Apply what you've learned about OOP, error handling, and clean code.", 20),
        TaskEntity(142, "Coding", "Coding", 22, "Learn about APIs – make one GET request", "Use fetch/axios/requests to call a free API. Print the response.", 20),
        TaskEntity(143, "Coding", "Coding", 23, "Solve 3 Easy LeetCode problems", "Speed and variety. Aim to finish each in under 8 minutes.", 25),
        TaskEntity(144, "Coding", "Coding", 24, "Build a simple REST API", "One endpoint: GET /hello returns JSON. Use Express, Flask, or Spring Boot.", 30),
        TaskEntity(145, "Coding", "Coding", 25, "Learn about SQL basics", "CREATE TABLE, INSERT, SELECT, WHERE, ORDER BY. Run queries on SQLite.", 25),
        TaskEntity(146, "Coding", "Coding", 26, "Solve a Hard LeetCode problem (any time)", "Read the problem, try for 20 min, then study the solution carefully.", 30),
        TaskEntity(147, "Coding", "Coding", 27, "Write unit tests for your CLI app", "Pick 3 functions and write tests. Learn what 'test coverage' means.", 25),
        TaskEntity(148, "Coding", "Coding", 28, "Read about system design basics", "Load balancing, caching, databases. Watch a YouTube video on 'system design interview'.", 20),
        TaskEntity(149, "Coding", "Coding", 29, "Build something small that solves YOUR problem", "Write a script that automates something tedious in your own life.", 30),
        TaskEntity(150, "Coding", "Coding", 30, "Write a technical blog post about what you learned", "Publish it anywhere – Medium, Dev.to, or a private journal. Teaching = mastery.", 30)
    )
}
