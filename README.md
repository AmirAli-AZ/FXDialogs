## FXDialogs

This is a library to create AlertDialog , ProgressDialog and etc...

### Examples

> Click ▶️ or 🔽 to expand or collapse examples

<details>
<summary>AlertDialog</summary>

```java
var alert = new AlertDialog.Builder()
        .setDialogTitle("Title")
        .setDialogMessage("Message")
        .setPositiveButton("Ok", System.out::println)
        .create();
alert.initModality(Modality.APPLICATION_MODAL);
alert.setTitle(alert.getDialogTitle());
alert.show();
```

<img src="images/alert-dialog-demo-1.png" alt="AlertDialog">
</details>

<details>
<summary>MultiChoiceAlertDialog</summary>

```java
var alert = new AlertDialog.Builder()
        .setDialogTitle("Title")
        .setDialogMessage("Message")
        .setPositiveButton("Ok", System.out::println)
        .setMultiChoiceItems(new String[]{"A","B","C","D","E"}, new Integer[]{0,2,3}, (which, isChecked) -> {
            if (isChecked)
                System.out.println(which + " checked");
            })
        .create();
alert.initModality(Modality.APPLICATION_MODAL);
alert.setTitle(alert.getDialogTitle());
alert.show();
```

<img src="images/alert-dialog-demo-2.png" alt="MultiChoiceAlertDialog">
</details>

<details>
<summary>SingleChoiceAlertDialog</summary>

```java
var alert = new AlertDialog.Builder()
        .setDialogTitle("Title")
        .setDialogMessage("Message")
        .setPositiveButton("Ok", System.out::println)
        .setSingleChoiceItems(new String[]{"A","B","C","D"}, 2, which -> System.out.println(which + "checked"))
        .create(); 
alert.initModality(Modality.APPLICATION_MODAL);
alert.setTitle(alert.getDialogTitle());
alert.show();
```

<img src="images/alert-diaog-demo-3.png" alt="SingleChoiceAlertDialog">
</details>

<details>
<summary>CustomAlertDialog</summary>

```java
var customNodeContainer = new HBox();
customNodeContainer.setPadding(new Insets(10));
var textField = new TextField();
HBox.setHgrow(textField, Priority.ALWAYS);
textField.setPromptText("Name");   
                       
customNodeContainer.getChildren().add(textField);

var alert = new AlertDialog.Builder()
        .setDialogTitle("Name")
        .setDialogMessage("Enter your name in below text field")
        .setPositiveButton("Ok", which -> System.out.println(textField.getText()))
        .setNode(customNodeContainer)
        .create();
alert.initModality(Modality.APPLICATION_MODAL);
alert.setTitle(alert.getDialogTitle());
alert.show();
```

<img src="images/alert-dialog-demo-4.png" alt="CustomAlertDialog">
</details>

<details>
<summary>AlertDialogWithSound</summary>

```java
var alert = new AlertDialog.Builder()   
        .setDialogTitle("DialogWithSound")
        .setDialogMessage("The sound will be play when dialog shown")
        .setPositiveButton("Ok", System.out::println)
        .setSound(Sounds.ChimesGlassy)
        .create();
alert.initModality(Modality.APPLICATION_MODAL);
alert.setTitle(alert.getDialogTitle());
alert.show();
```

Note : you should add javafx.media dependency when you want to use sounds
</details>


<details>
<summary>TimePickerDialog</summary>

```java
var timePickerDialog = new TimePickerDialog.Builder()
        .create();
timePickerDialog.initModality(Modality.APPLICATION_MODAL);
timePickerDialog.showAndWait();

System.out.println(timePickerDialog.getTime().toString());
```

<img src="images/timepicker-dialog-demo.png" alt="TimePickerDialog">
</details>

<details>
<summary>ProgressDialog</summary>

```java
var progressDialog = new ProgressDialog.Builder()
        .setDialogTitle("Title")
        .setDialogMessage("Message")
        .setProgressType(ProgressDialog.ProgressBarType.Bar)
        .setProgress(.5)
        .create();
progressDialog.initModality(Modality.APPLICATION_MODAL);
progressDialog.setTitle(progressDialog.getDialogTitle());
progressDialog.show();
```

<img src="images/progress-dialog-demo.png" alt="ProgressDialog">
</details>

<details>
<summary>ExceptionDialog</summary>

```java
try {
    System.out.println(20/0);
}catch (ArithmeticException e) {
    var exceptionDialog = new ExceptionDialog.Builder()
            .setDialogMessage(e.getMessage())
            .setException(e)
            .create();
    exceptionDialog.show();
}
```

<img src="images/exception-dialog-demo.png" alt="ExceptionDialog">
</details>

<details>
<summary>PersistentBottomSheet</summary>

```java
var root = new BorderPane();

var persistentBottomSheet = new PersistentBottomSheet();
persistentBottomSheet.setPrefHeight(150);
persistentBottomSheet.setStyle("-fx-background-color : orange;");
persistentBottomSheet.setDuration(Duration.seconds(.5));
persistentBottomSheet.dragHandlerImageProperty().set(new Image(Objects.requireNonNull(getClass().getResourceAsStream("round_horizontal_rule_white_24dp.png"))));
persistentBottomSheet.addSupportResizing();

var label = new Label("Bottom Sheet");
label.setStyle("-fx-text-fill : white; -fx-font-size : 18px;");
var bottomSheetContentRoot = new StackPane(label);
bottomSheetContentRoot.setPadding(new Insets(15));

persistentBottomSheet.getChildren().add(bottomSheetContentRoot);

var showButton = new Button("Hide");
showButton.setPrefSize(75, 25);
showButton.setOnAction(event -> {
    persistentBottomSheet.showingProperty().set(!persistentBottomSheet.isShowing());
        if (persistentBottomSheet.isShowing())
            showButton.setText("Hide");
        else
            showButton.setText("Show");
    });

root.setCenter(new StackPane(showButton));
root.setBottom(persistentBottomSheet);
```

BottomSheetCallBack

```java
persistentBottomSheet.setCallBack(new BottomSheetCallBack() {
    @Override
    public void onState(PersistentBottomSheet bottomSheet, int state) {
        switch (state) {
            case PersistentBottomSheet.EXPANDED ->
                    System.out.println("expanded");
            case PersistentBottomSheet.COLLAPSED ->
                    System.out.println("collapsed");
            case PersistentBottomSheet.DRAGGED -> 
                    System.out.println("dragged");
            case PersistentBottomSheet.HIDDEN ->
                    System.out.println("hidden");
            case PersistentBottomSheet.SHOWN ->
                    System.out.println("shown");
        }
    }

    @Override
    public void onResized(PersistentBottomSheet bottomSheet, int percent) {
        System.out.println(percent + "%");
    }
});
```

<img src="images/persistent-bottom-sheet-demo-1.png" alt="PersistentBottomSheetDemo">
<img src="images/persistent-bottom-sheet-demo-2.png" alt="PersistentBottomSheetDemo">

</details>


### Styling

You can use `.setStyles(String... styles)` to add custom css styles to dialog

example :

```
.root {
    -fx-background-color : white;
}

#title {
    -fx-font-size : 18px;
    -fx-font-weight : bold;
}

#message {
    -fx-font-size : 16px;
}
```
