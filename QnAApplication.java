import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


class Post implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 0;
    private int id;
    private String content;

    public Post(String content) {
        this.id = ++idCounter;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}

class Question extends Post {
    private static final long serialVersionUID = 1L;
    private String title;

    public Question(String title, String content) {
        super(content);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

class Answer extends Post {
    private static final long serialVersionUID = 1L;
    private int questionId;

    public Answer(int questionId, String content) {
        super(content);
        this.questionId = questionId;
    }

    public int getQuestionId() {
        return questionId;
    }
}

public class QnAApplication implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "qna_data.ser";
    private HashMap<Integer, Question> questions;
    private ArrayList<Answer> answers;

    public QnAApplication() {
        questions = new HashMap<>();
        answers = new ArrayList<>();
    }

    public void postQuestion(String title, String content) {
        Question question = new Question(title, content);
        questions.put(question.getId(), question);
        saveData();
    }

    public void postAnswer(int questionId, String content) {
        if (!questions.containsKey(questionId)) {
            System.out.println("Question ID does not exist!");
            return;
        }
        Answer answer = new Answer(questionId, content);
        answers.add(answer);
        saveData();
    }

    public void start(Stage primaryStage) {
        // Create layout components
        VBox layout = new VBox(10); // Vertical layout with spacing
        TextArea outputArea = new TextArea(); // For displaying questions and answers
        outputArea.setEditable(false);

        // Input fields for posting a question
        TextField questionTitleField = new TextField();
        questionTitleField.setPromptText("Enter question title");
        TextArea questionContentArea = new TextArea();
        questionContentArea.setPromptText("Enter question content");
        Button postQuestionButton = new Button("Post Question");
        Button refreshButton = new Button("Refresh Questions");
        refreshButton.setOnAction(event -> displayQuestions(outputArea));
        // Input fields for posting an answer
        TextField answerQuestionIdField = new TextField();
        answerQuestionIdField.setPromptText("Enter question ID to answer");
        TextArea answerContentArea = new TextArea();
        answerContentArea.setPromptText("Enter answer content");
        Button postAnswerButton = new Button("Post Answer");

        // Event handler for posting a question
        // Inside your start method after setting up UI components
        postQuestionButton.setOnAction(event -> {
            String title = questionTitleField.getText();
            String content = questionContentArea.getText();
        if (!title.isEmpty() && !content.isEmpty()) {
            postQuestion(title, content); // Method to add a question
            displayQuestions(outputArea); // Refresh the display
            }
    }
    );

postAnswerButton.setOnAction(event -> {
    try {
        int questionId = Integer.parseInt(answerQuestionIdField.getText());
        String answerContent = answerContentArea.getText();
        if (!answerContent.isEmpty()) {
            postAnswer(questionId, answerContent); // Method to add an answer
            displayQuestions(outputArea); // Refresh the display
        }
    } catch (NumberFormatException e) {
        // Handle invalid input for question ID
    }
});

// And in your initialization phase
displayQuestions(outputArea); // To display questions and answers initially

    // Add components to layout
    layout.getChildren().addAll(
        new Label("Post a Question:"),
        questionTitleField,
        questionContentArea,
        postQuestionButton,
        new Label("Post an Answer:"),
        answerQuestionIdField,
        answerContentArea,
        postAnswerButton,
        new Label("Questions and Answers:"),
        outputArea
    );

    // Setup and display the primary stage
    Scene scene = new Scene(layout, 600, 600);
    primaryStage.setTitle("Q&A Application");
    primaryStage.setScene(scene);
    primaryStage.show();

    // Load and display existing questions and answers
    displayQuestions(outputArea);
}

// Method to display questions and answers in the TextArea
private void displayQuestions(TextArea outputArea) {
    StringBuilder output = new StringBuilder();
    for (Question q : this.getQuestionsSortedById()) {
        output.append("ID: ").append(q.getId()).append(" Title: ").append(q.getTitle()).append("\n");
        output.append(q.getContent()).append("\n");
        List<Answer> relatedAnswers = this.answers.stream()
                .filter(a -> a.getQuestionId() == q.getId())
                .collect(Collectors.toList());
        for (Answer a : relatedAnswers) {
            output.append("\tAnswer ID: ").append(a.getId()).append(" Content: ").append(a.getContent()).append("\n");
        }
        output.append("\n");
    }
    outputArea.setText(output.toString());
}


    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(this);
        } catch (IOException e) {
            System.out.println("An error occurred while saving data: " + e.getMessage());
        }
    }
       public List<Question> searchQuestionsByTitle(String title) {
        return questions.values().stream()
                .filter(q -> q.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Question> getQuestionsSortedById() {
        return questions.values().stream()
                .sorted(Comparator.comparingInt(Question::getId))
                .collect(Collectors.toList());
    }
    private static QnAApplication loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            return (QnAApplication) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Data file not found, starting a new session.");
            return new QnAApplication();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("An error occurred while loading data: " + e.getMessage());
            return new QnAApplication();
        }
    }
   public void printQuestionsToConsole() {
    for (Question q : questions.values()) {
        System.out.println("Question ID " + q.getId() + ": " + q.getTitle());
        System.out.println(q.getContent());
        
        // Find and print answers related to this question
        List<Answer> relatedAnswers = answers.stream()
            .filter(a -> a.getQuestionId() == q.getId())
            .collect(Collectors.toList());

        if (relatedAnswers.isEmpty()) {
            System.out.println("No answers for this question yet.");
        } else {
            for (Answer a : relatedAnswers) {
                System.out.println("\tAnswer: " + a.getContent());
            }
        }
        System.out.println();
    }
}


private void saveQuestionsToTextFile() {
    try (PrintWriter out = new PrintWriter(new FileOutputStream("questions.txt"))) {
        for (Question q : questions.values()) {
            out.println(q.getId() + "|" + q.getTitle() + "|" + q.getContent());
            // '|' is used as a delimiter
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void saveAnswersToTextFile() {
    try (PrintWriter out = new PrintWriter(new FileOutputStream("answers.txt"))) {
        for (Answer a : answers) {
            out.println(a.getQuestionId() + "|" + a.getId() + "|" + a.getContent());
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void loadQuestionsFromTextFile(String filePath) {
    File questionsFile = new File(filePath);
    if (!questionsFile.exists()) {
        System.out.println("Questions file not found at " + filePath + ". Skipping load.");
        return;
    }

    try (Scanner in = new Scanner(new FileInputStream(questionsFile))) {
        questions = new HashMap<>();
        int nextId = 1; // Start ID numbering from 1
        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] parts = line.split("\\|");
            int id;
            if (parts.length >= 3) {
                try {
                    // Attempt to use provided ID
                    id = Integer.parseInt(parts[0]);
                } catch (NumberFormatException e) {
                    // If ID is not valid, assign a new ID
                    id = nextId;
                }
                Question q = new Question(parts[1], parts[2]);
                questions.put(id, q);
                System.out.println("Loaded Question ID " + id + ": " + q.getTitle());
                nextId = Math.max(nextId, id + 1); // Ensure nextId is always greater than any loaded ID
            }
        }
    } catch (FileNotFoundException e) {
        System.out.println("Questions file not found at " + filePath + ": " + e.getMessage());
    } catch (IOException e) {
        System.out.println("Error reading questions file at " + filePath + ": " + e.getMessage());
    }
}


private void loadAnswersFromTextFile(String filePath) {
    File answersFile = new File(filePath);
    if (!answersFile.exists()) {
        System.out.println("Answers file not found at " + filePath + ". Skipping load.");
        return;
    }

    try (Scanner in = new Scanner(new FileInputStream(answersFile))) {
        answers = new ArrayList<>();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] parts = line.split("\\|");
            if (parts.length >= 3) {
                int questionId = Integer.parseInt(parts[0]);
                Answer a = new Answer(questionId, parts[2]);
                answers.add(a);
            }
        }
    } catch (FileNotFoundException e) {
        System.out.println("Answers file not found at " + filePath + ": " + e.getMessage());
    } catch (IOException | NumberFormatException e) {
        System.out.println("Error reading answers file at " + filePath + ": " + e.getMessage());
    }
}



    public static void main(String[] args) {
    QnAApplication app = loadData();

    try (Scanner scanner = new Scanner(System.in)) {
        String input;
        do {
            System.out.println("\nChoose an option:");
            System.out.println("(1) List all questions");
            System.out.println("(2) Post a new question");
            System.out.println("(3) Post an answer to a question");
            System.out.println("(4) Load questions from file");
            System.out.println("(5) Save questions to file");
            System.out.println("(0) Exit");
            System.out.print("Enter your choice: ");
            input = scanner.nextLine();

            switch (input) {
                case "1":
                    app.printQuestionsToConsole();
                    break;
                case "2":
                    System.out.print("Enter your question title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter your question content: ");
                    String content = scanner.nextLine();
                    app.postQuestion(title, content);
                    break;
                case "3":
                    app.printQuestionsToConsole();
                    System.out.print("Enter the ID of the question you want to answer: ");
                    try {
                        int questionId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter your answer: ");
                        String answerContent = scanner.nextLine();
                        app.postAnswer(questionId, answerContent);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid question ID. Please enter a valid number.");
                    }
                    break;
                case "4":
                    System.out.print("Enter the path for the questions file: ");
                    String questionsPath = scanner.nextLine();
                    app.loadQuestionsFromTextFile(questionsPath);

                    System.out.print("Enter the path for the answers file: ");
                    String answersPath = scanner.nextLine();
                    app.loadAnswersFromTextFile(answersPath);
                    break;
                case "5":
                    app.saveQuestionsToTextFile();
                    app.saveAnswersToTextFile();
                    break;
                case "0":
                    System.out.println("Exiting application.");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        } while (!input.equals("0"));
    } catch (Exception e) {
        System.out.println("An unexpected error occurred: " + e.getMessage());
    }
}    
}
