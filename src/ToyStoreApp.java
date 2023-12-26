import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

interface Toy {
    int getId();
    String getName();
    int getQuantity();
    double getWeight();
    void setQuantity(int quantity);
    void setWeight(double weight);
}

class ToyImplementation implements Toy {
    private int id;
    private String name;
    private int quantity;
    private double weight;

    public ToyImplementation(int id, String name, int quantity, double weight) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }
}

interface ToyGame {
    Toy play();
}

class ToyGameImplementation implements ToyGame {
    private List<Toy> toys = new ArrayList<>();

    public void addToy(Toy toy) {
        toys.add(toy);
    }

    public void updateWeight(int toyId, double newWeight) {
        for (Toy toy : toys) {
            if (toy.getId() == toyId) {
                toy.setWeight(newWeight);
                return;
            }
        }
        System.out.println("Игрушка с ID " + toyId + " не найдена.");
    }

    @Override
    public Toy play() {
        double randomValue = Math.random() * 100;
        double currentWeight = 0;

        for (Toy toy : toys) {
            currentWeight += toy.getWeight();
            if (randomValue <= currentWeight) {
                if (toy.getQuantity() > 0) {
                    toy.setQuantity(toy.getQuantity() - 1);
                    return toy;
                }
            }
        }

        return null;
    }
}

interface ToyFileWriter {
    void saveToFile(Toy toy);
}

class ToyFileWriterImplementation implements ToyFileWriter {
    @Override
    public void saveToFile(Toy toy) {
        try (FileWriter writer = new FileWriter("winners.txt", true)) {
            writer.write("ID: " + toy.getId() + ", Название: " + toy.getName() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class ToyStoreApp {
    public static void main(String[] args) {
        ToyGame toyGame = new ToyGameImplementation();
        ToyFileWriter toyFileWriter = new ToyFileWriterImplementation();

        Toy toy1 = new ToyImplementation(1, "Кукла", 10, 20);
        Toy toy2 = new ToyImplementation(2, "Машинка", 15, 30);
        Toy toy3 = new ToyImplementation(3, "Мяч", 5, 50);

        ((ToyGameImplementation) toyGame).addToy(toy1);
        ((ToyGameImplementation) toyGame).addToy(toy2);
        ((ToyGameImplementation) toyGame).addToy(toy3);

        ((ToyGameImplementation) toyGame).updateWeight(1, 10);  // Изменяем вес куклы
        ((ToyGameImplementation) toyGame).updateWeight(2, 20);  // Изменяем вес машинки
        ((ToyGameImplementation) toyGame).updateWeight(3, 30);  // Изменяем вес мяча

        Toy winner = toyGame.play();
        if (winner != null) {
            System.out.println("Выиграла игрушка: " + winner.getName());
            toyFileWriter.saveToFile(winner);
        } else {
            System.out.println("Нет выигравшей игрушки.");
        }
    }
}
