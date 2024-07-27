package javafx_app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MonsterFighterApp extends Application {

    private int playerHealth = 100;
    private int monsterHealth = 100;
    private ImageView playerImageView;
    private ImageView monsterImageView;
    private ImageView playerSkillEffectImageView;
    private ImageView monsterSkillEffectImageView;
    private Label playerHealthLabel;
    private Label monsterHealthLabel;
    private Timeline monsterAttackTimeline;

    @Override
    public void start(Stage primaryStage) {
        // 创建玩家和怪兽的图片
        playerImageView = new ImageView(new Image("file:C:/Users/GeekGuru/Downloads/java-swing-master/untitled/src/resources/player.png"));
        playerImageView.setFitWidth(150);
        playerImageView.setFitHeight(150);
        playerImageView.setLayoutX(100); // 设置初始位置
        playerImageView.setLayoutY(300);

        monsterImageView = new ImageView(new Image("file:C:/Users/GeekGuru/Downloads/java-swing-master/untitled/src/resources/monster.png"));
        monsterImageView.setFitWidth(150);
        monsterImageView.setFitHeight(150);
        monsterImageView.setLayoutX(700); // 设置初始位置
        monsterImageView.setLayoutY(300);

        // 创建技能特效图片
        playerSkillEffectImageView = new ImageView();
        playerSkillEffectImageView.setFitWidth(150);
        playerSkillEffectImageView.setFitHeight(150);

        monsterSkillEffectImageView = new ImageView();
        monsterSkillEffectImageView.setFitWidth(150);
        monsterSkillEffectImageView.setFitHeight(150);

        // 创建玩家和怪兽的健康标签
        playerHealthLabel = new Label("Health: " + playerHealth);
        StackPane playerStack = new StackPane(playerImageView, playerSkillEffectImageView);
        VBox playerBox = new VBox(10, playerStack, playerHealthLabel);

        monsterHealthLabel = new Label("Health: " + monsterHealth);
        StackPane monsterStack = new StackPane(monsterImageView, monsterSkillEffectImageView);
        VBox monsterBox = new VBox(10, monsterStack, monsterHealthLabel);

        // 创建技能按钮
        Button playerSkillButton = new Button("Player Skill");
        playerSkillButton.setOnAction(e -> useSkill("player"));

        Button monsterSkillButton = new Button("Monster Skill");
        monsterSkillButton.setOnAction(e -> useSkill("monster"));

        VBox skillBox = new VBox(20, playerSkillButton, monsterSkillButton);
        skillBox.setStyle("-fx-alignment: center;");

        // 创建布局
        Pane gamePane = new Pane(playerImageView, monsterImageView, playerSkillEffectImageView, monsterSkillEffectImageView);
        BorderPane root = new BorderPane();
        root.setLeft(playerBox);
        root.setRight(monsterBox);
        root.setCenter(skillBox);
        root.setBottom(gamePane);

        // 创建场景并设置舞台
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Monster Fighter");
        primaryStage.setScene(scene);
        primaryStage.show();

        // 添加键盘事件处理程序
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case A -> movePlayer(-10, 0);
                case D -> movePlayer(10, 0);
                case W -> movePlayer(0, -10);
                case S -> movePlayer(0, 10);
                case LEFT -> moveMonster(-10, 0);
                case RIGHT -> moveMonster(10, 0);
                case UP -> moveMonster(0, -10);
                case DOWN -> moveMonster(0, 10);
                case SPACE -> useSkill("player");
            }
        });

        // 怪兽自动攻击
        monsterAttackTimeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> useSkill("monster")));
        monsterAttackTimeline.setCycleCount(Timeline.INDEFINITE);
        monsterAttackTimeline.play();
    }

    private void useSkill(String user) {
        int damage = (int) (Math.random() * 10) + 1;
        if (user.equals("player")) {
            monsterHealth -= damage;
            monsterHealthLabel.setText("Health: " + monsterHealth);
            showSkillEffect(playerImageView, monsterImageView, "fire");
        } else {
            playerHealth -= damage;
            playerHealthLabel.setText("Health: " + playerHealth);
            showSkillEffect(monsterImageView, playerImageView, "ice");
        }

        if (playerHealth <= 0) {
            playerHealthLabel.setText("Health: 0 (You lost!)");
        } else if (monsterHealth <= 0) {
            monsterHealthLabel.setText("Health: 0 (You won!)");
            monsterAttackTimeline.stop();
        }
    }

    private void showSkillEffect(ImageView attacker, ImageView target, String effectType) {
        String[] skillImages = new String[16];
        for (int i = 0; i < 16; i++) {
            skillImages[i] = "file:C:/Users/GeekGuru/Downloads/java-swing-master/untitled/src/monster_fighting_game/resources/" + effectType + "/" + effectType + "(" + (i + 1) + ").png";
        }

        Timeline timeline = new Timeline();
        for (int i = 0; i < skillImages.length; i++) {
            Image img = new Image(skillImages[i]);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * 100), e -> {
                if (effectType.equals("fire")) {
                    monsterSkillEffectImageView.setImage(img);
                    monsterSkillEffectImageView.setLayoutX(target.getLayoutX());
                    monsterSkillEffectImageView.setLayoutY(target.getLayoutY());
                } else {
                    playerSkillEffectImageView.setImage(img);
                    playerSkillEffectImageView.setLayoutX(target.getLayoutX());
                    playerSkillEffectImageView.setLayoutY(target.getLayoutY());
                }
            });
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.setOnFinished(e -> {
            if (effectType.equals("fire")) {
                monsterSkillEffectImageView.setImage(null);
            } else {
                playerSkillEffectImageView.setImage(null);
            }
        });
        timeline.play();
    }

    private void movePlayer(int deltaX, int deltaY) {
        playerImageView.setLayoutX(playerImageView.getLayoutX() + deltaX);
        playerImageView.setLayoutY(playerImageView.getLayoutY() + deltaY);
    }

    private void moveMonster(int deltaX, int deltaY) {
        monsterImageView.setLayoutX(monsterImageView.getLayoutX() + deltaX);
        monsterImageView.setLayoutY(monsterImageView.getLayoutY() + deltaY);
    }

    public static void main(String[] args) {
        launch(args);
    }
}