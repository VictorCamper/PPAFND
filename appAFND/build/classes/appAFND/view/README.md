##### appAFND.view package

Notas:
- Clases para la visualización de datos o interfaces, además de archivos `.FXML`

Ejemplo:

```java
public class StudentView {
   public void printStudentDetails(String studentName, String studentRollNo){
      System.out.println("Student: ");
      System.out.println("Name: " + studentName);
      System.out.println("Roll No: " + studentRollNo);
   }
}
```