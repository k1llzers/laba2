import models.Goods;
import models.Group;
import models.Storage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Main {
    private static JPanel currentPanel;
    private static Storage storage;
    private final static JFrame frame = new JFrame("Склад");
    private static int isChanges = 2;
    private final static JFrame exceptionFrame = new JFrame();

    public static void main(String[] args){
        readFromFile();
        frame.setSize(500,450);
        JPanel start = new JPanel();
        start.setBounds(0,0,500,450);
        start.setLayout(null);
        BufferedImage image;
        try {
            image = ImageIO.read(new File("storage.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JLabel background = new JLabel(new ImageIcon(image));
        background.setBounds(0,0,500,450);
        start.add(background);
        JButton buttonStart = new JButton("Почати роботу");
        buttonStart.setBounds(175, 200, 150, 50);
        buttonStart.setBackground(Color.decode("#D4E6F1"));
        try {
            buttonStart.addActionListener(e -> {
                frame.remove(start);
                menu();
            });
        } catch (RuntimeException e){
            JOptionPane.showMessageDialog(frame,e.getMessage());
        }
        background.add(buttonStart);
        frame.add(start);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                saveToFile();
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void readFromFile(){
        try {
            File fileFrom = new File("storage.txt");
            FileInputStream fileInputStream = new FileInputStream(fileFrom);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            storage = (Storage) objectInputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveToFile(){
        try {
            File fileTo = new File("storage.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(fileTo);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(storage);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void menu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu change = new JMenu("Редагувати");
        JMenuItem groups = new JMenuItem("Групи");
        groups.addActionListener(e -> changeGroup());
        JMenuItem goods = new JMenuItem("Товари");
        goods.addActionListener(e -> {
            if (storage.getAllGroups().size() == 0)
                JOptionPane.showMessageDialog(exceptionFrame,"Жодного товару не існує!");
            else changeGoods();
        });
        change.add(goods);
        change.add(groups);
        menuBar.add(change);
        JButton search = new JButton("Пошук");
        search.addActionListener(e -> {
            if (storage.getAllGroups().size() == 0)
                JOptionPane.showMessageDialog(exceptionFrame,"Жодного товару не існує!");
            else if (storage.getAllGoods().size() == 0)
                JOptionPane.showMessageDialog(exceptionFrame,"Жодного товару не існує!");
            else
                search();
        });
        menuBar.add(search);
        JMenu data = new JMenu("Вивід даних");
        JMenuItem allGoods = new JMenuItem("Всі товари");
        allGoods.addActionListener(e -> getInformationOnStorage());
        JMenuItem byGroup = new JMenuItem("Всі товари по групі");
        byGroup.addActionListener(e -> {
            if (storage.getAllGroups().size() == 0)
                JOptionPane.showMessageDialog(exceptionFrame,"Жодної групи не існує!");
            else
                getInformationByGroup();
        });
        JMenuItem allGoodsCost = new JMenuItem("Вартість всіх товарів");
        allGoodsCost.addActionListener(e -> allGoodsCostInformation());
        JMenuItem groupCost = new JMenuItem("Вартість всіх товарів групи");
        groupCost.addActionListener(e -> allGoodsCostInformationByGroup());
        data.add(allGoods);
        data.add(byGroup);
        data.add(allGoodsCost);
        data.add(groupCost);
        menuBar.add(data);
        JButton addGoods = new JButton("Додати товар");
        addGoods.addActionListener(e -> {
            if (storage.getAllGoods().size() == 0)
                JOptionPane.showMessageDialog(exceptionFrame,"Жодного товару не існує!");
            else
                addGoods();
        });
        menuBar.add(addGoods);
        JButton removeGoods = new JButton("Списати товар");
        removeGoods.addActionListener(e -> {
            if (storage.getAllGoods().size() == 0)
                JOptionPane.showMessageDialog(exceptionFrame,"Жодного товару не існує!");
            else
                removeGoods();
        });
        menuBar.add(removeGoods);
        frame.setJMenuBar(menuBar);
        menuBar.updateUI();
    }


    private static void changeGroup(){
        if (currentPanel != null){
            frame.remove(currentPanel);
        }
        JPanel changeGroupPanel = new JPanel(new GridLayout(2,1));
        JList<Group> listOfGroup = new JList<>();
        Group[] groups = new Group[storage.getAllGroups().size()];
        storage.getAllGroups().toArray(groups);
        listOfGroup.setListData(groups);
        JScrollPane listScroll = new JScrollPane(listOfGroup);
        listScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        changeGroupPanel.add(listScroll);
        frame.add(changeGroupPanel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(450,185));
        SpringLayout layout = new SpringLayout();
        buttonPanel.setLayout(layout);
        JScrollPane buttonPanelScroll = new JScrollPane(buttonPanel);
        buttonPanelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        buttonPanelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JButton add = new JButton("Додати");
        add.setPreferredSize(new Dimension(140,50));
        add.setMinimumSize(new Dimension(20,50));
        layout.putConstraint(SpringLayout.WEST, add, 10, SpringLayout.WEST, buttonPanel);
        layout.putConstraint(SpringLayout.NORTH, add, 10, SpringLayout.NORTH, buttonPanel);
        JButton change = new JButton("Редагувати");
        change.setPreferredSize(new Dimension(140,50));
        layout.putConstraint(SpringLayout.WEST, change, 10, SpringLayout.EAST, add);
        layout.putConstraint(SpringLayout.NORTH, change, 10, SpringLayout.NORTH, buttonPanel);
        JButton delete = new JButton("Видалити");
        delete.setPreferredSize(new Dimension(140,50));
        layout.putConstraint(SpringLayout.WEST, delete, 10, SpringLayout.EAST, change);
        layout.putConstraint(SpringLayout.NORTH, delete, 10, SpringLayout.NORTH, buttonPanel);
        JLabel nameLabel = new JLabel("Назва:");
        nameLabel.setPreferredSize(new Dimension( 150, 20));
        layout.putConstraint(SpringLayout.WEST, nameLabel, 50, SpringLayout.WEST, buttonPanel);
        layout.putConstraint(SpringLayout.NORTH, nameLabel, 10, SpringLayout.SOUTH, add);
        JTextArea name = new JTextArea();
        name.setPreferredSize(new Dimension(150, 50));
        layout.putConstraint(SpringLayout.WEST, name, 50, SpringLayout.WEST, buttonPanel);
        layout.putConstraint(SpringLayout.NORTH, name, 5, SpringLayout.SOUTH, nameLabel);
        JLabel descriptionLabel = new JLabel("Опис:");
        descriptionLabel.setPreferredSize(new Dimension(150, 20));
        layout.putConstraint(SpringLayout.WEST, descriptionLabel, 100, SpringLayout.EAST, nameLabel);
        layout.putConstraint(SpringLayout.NORTH, descriptionLabel, 10, SpringLayout.SOUTH, delete);
        JButton submit = new JButton("Підтвердити");
        submit.setPreferredSize(new Dimension(150, 30));
        layout.putConstraint(SpringLayout.WEST, submit, 50, SpringLayout.WEST, buttonPanel);
        layout.putConstraint(SpringLayout.NORTH, submit, 10, SpringLayout.SOUTH, name);
        JButton cancel = new JButton("Відмінити");
        cancel.setPreferredSize(new Dimension(150, 30));
        layout.putConstraint(SpringLayout.WEST, cancel, 100, SpringLayout.EAST, submit);
        layout.putConstraint(SpringLayout.NORTH, cancel, 10, SpringLayout.SOUTH, name);
        buttonPanel.add(add);
        buttonPanel.add(change);
        buttonPanel.add(delete);
        name.setLineWrap(true);
        name.setWrapStyleWord(true);
        JTextArea description = new JTextArea();
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(description);
        descriptionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        descriptionScroll.setPreferredSize(new Dimension(150, 50));
        layout.putConstraint(SpringLayout.WEST, descriptionScroll, 100, SpringLayout.EAST, name);
        layout.putConstraint(SpringLayout.NORTH, descriptionScroll, 5, SpringLayout.SOUTH, descriptionLabel);
        ArrayList<JComponent> buttonPanelComponent = new ArrayList<>();
        buttonPanelComponent.add(name);
        buttonPanelComponent.add(nameLabel);
        buttonPanelComponent.add(descriptionLabel);
        buttonPanelComponent.add(submit);
        buttonPanelComponent.add(cancel);
        buttonPanelComponent.add(descriptionScroll);
        buttonPanelComponent.forEach(buttonPanel::add);
        buttonPanelComponent.forEach(e -> e.setVisible(false));
        add.addActionListener(e -> {
            buttonPanelComponent.forEach(c -> c.setVisible(true));
            isChanges = 1;
        });
        change.addActionListener(e -> {
            buttonPanelComponent.forEach(c -> c.setVisible(true));
            isChanges = 2;
        });
        delete.addActionListener(e -> {
            submit.setVisible(true);
            cancel.setVisible(true);
            isChanges = 3;
        });
        submit.addActionListener(e -> {
            if (isChanges == 1){
                if(name.getText().equals("") || description.getText().equals("")){
                    name.setText("");
                    description.setText("");
                    buttonPanelComponent.forEach(c -> c.setVisible(false));
                    JOptionPane.showMessageDialog(exceptionFrame,"Є пусті поля!");
                    return;
                }
                try {
                    storage.add(new Group(name.getText(), description.getText()));
                } catch (Exception ex) {
                    name.setText("");
                    description.setText("");
                    buttonPanelComponent.forEach(c -> c.setVisible(false));
                    JOptionPane.showMessageDialog(exceptionFrame,"Така група товарів вже існує!");
                    return;
                }
                Group[] groups1 = new Group[storage.getAllGroups().size()];
                storage.getAllGroups().toArray(groups1);
                listOfGroup.setListData(groups1);
            }
            if (isChanges == 2){
                if (listOfGroup.getSelectedIndex() == - 1){
                    name.setText("");
                    description.setText("");
                    buttonPanelComponent.forEach(c -> c.setVisible(false));
                    JOptionPane.showMessageDialog(exceptionFrame,"Жоден алемент не вибрано!");
                    return;
                }
                storage.edit(listOfGroup.getSelectedValue().getName(),new Group(name.getText(),description.getText()));
                Group[] groups1 = new Group[storage.getAllGroups().size()];
                storage.getAllGroups().toArray(groups1);
                listOfGroup.setListData(groups1);
            }
            if (isChanges == 3){
                if (listOfGroup.getSelectedIndex() == - 1){
                    buttonPanelComponent.forEach(c -> c.setVisible(false));
                    JOptionPane.showMessageDialog(exceptionFrame,"Жоден алемент не вибрано!");
                    return;
                }
                storage.delete(listOfGroup.getSelectedValue().getName());
                Group[] groups1 = new Group[storage.getAllGroups().size()];
                storage.getAllGroups().toArray(groups1);
                listOfGroup.setListData(groups1);
            }
            name.setText("");
            description.setText("");
            buttonPanelComponent.forEach(c -> c.setVisible(false));
            changeGroupPanel.setVisible(false);
            changeGroupPanel.setVisible(true);
        });
        cancel.addActionListener(e -> {
            name.setText("");
            description.setText("");
            buttonPanelComponent.forEach(c -> c.setVisible(false));
            changeGroupPanel.setVisible(false);
            changeGroupPanel.setVisible(true);
        });
        changeGroupPanel.add(buttonPanelScroll);
        currentPanel = changeGroupPanel;
        frame.setVisible(true);
    }

    private static void changeGoods(){
        if (currentPanel != null){
            frame.remove(currentPanel);
        }
        JPanel changeGoodsPanel = new JPanel(new GridLayout(2,1));
        JPanel allGoods = new JPanel(new GridLayout(1,2));
        JList<Goods> goodsList = new JList<Goods>();
        Group firstGroup = (Group) storage.getAllGroups().get(0);
        Goods[] goods = new Goods[firstGroup.getAll().size()];
        firstGroup.getAll().toArray(goods);
        goodsList.setListData(goods);
        JScrollPane goodsScroll = new JScrollPane(goodsList);
        goodsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        goodsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        allGoods.add(goodsScroll);
        changeGoodsPanel.add(allGoods);
        SpringLayout springLayout = new SpringLayout();
        JPanel goodDescriptionPanel = new JPanel(springLayout);
        JComboBox<String> groupBox = new JComboBox<String>();
        storage.getAllGroups().stream().map(Group::getName).forEach(groupBox::addItem);
        groupBox.setSelectedIndex(0);
        groupBox.setPreferredSize(new Dimension(230,20));
        springLayout.putConstraint(SpringLayout.WEST, groupBox, 5, SpringLayout.WEST, goodDescriptionPanel);
        springLayout.putConstraint(SpringLayout.NORTH, groupBox, 5, SpringLayout.NORTH, goodDescriptionPanel);
        groupBox.addActionListener(e -> {
            if (e.getActionCommand().equals("comboBoxChanged")){
                Goods[] goods1 = new Goods[storage.getAllGroups().get(groupBox.getSelectedIndex()).getAll().size()];
                storage.getAllGroups().get(groupBox.getSelectedIndex()).getAll().toArray(goods1);
                goodsList.setListData(goods1);
            }
        });
        goodDescriptionPanel.add(groupBox);
        JTextArea description = new JTextArea();
        description.setPreferredSize(new Dimension(230, 155));
        springLayout.putConstraint(SpringLayout.WEST, description, 5, SpringLayout.WEST, goodDescriptionPanel);
        springLayout.putConstraint(SpringLayout.NORTH, description, 5, SpringLayout.SOUTH, groupBox);
        goodDescriptionPanel.add(description);
        description.setEditable(false);
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        goodsList.addListSelectionListener(e -> {
            if (goodsList.getSelectedValue() != null)
                description.setText("Опис: " + goodsList.getSelectedValue().getDescription() + "\n" + "Виробник: " + goodsList.getSelectedValue().getProducer()
                        + "\n" + "Ціна за одиницю: " + goodsList.getSelectedValue().getPrice());
        });
        goodDescriptionPanel.setPreferredSize(new Dimension(210,180));
        JScrollPane goodDescriptionScroll = new JScrollPane(goodDescriptionPanel);
        goodDescriptionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        goodDescriptionScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        allGoods.add(goodDescriptionScroll);
        SpringLayout layout = new SpringLayout();
        JPanel buttonPanel = new JPanel(layout);
        buttonPanel.setPreferredSize(new Dimension(450,185));
        JScrollPane buttonPanelScroll = new JScrollPane(buttonPanel);
        buttonPanelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        buttonPanelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JButton add = new JButton("Додати");
        add.setPreferredSize(new Dimension(140,20));
        layout.putConstraint(SpringLayout.WEST, add, 10, SpringLayout.WEST, buttonPanel);
        layout.putConstraint(SpringLayout.NORTH, add, 10, SpringLayout.NORTH, buttonPanel);
        JButton changeGood = new JButton("Редагувати");
        changeGood.setPreferredSize(new Dimension(140,20));
        layout.putConstraint(SpringLayout.WEST, changeGood, 10, SpringLayout.EAST, add);
        layout.putConstraint(SpringLayout.NORTH, changeGood, 10, SpringLayout.NORTH, buttonPanel);
        JButton delete = new JButton("Видалити");
        delete.setPreferredSize(new Dimension(140,20));
        layout.putConstraint(SpringLayout.WEST, delete, 10, SpringLayout.EAST, changeGood);
        layout.putConstraint(SpringLayout.NORTH, delete, 10, SpringLayout.NORTH, buttonPanel);
        JLabel nameLabel = new JLabel("Назва:");
        nameLabel.setPreferredSize(new Dimension( 80, 20));
        layout.putConstraint(SpringLayout.WEST, nameLabel, 20, SpringLayout.WEST, buttonPanel);
        layout.putConstraint(SpringLayout.NORTH, nameLabel, 5, SpringLayout.SOUTH, add);
        JTextArea name = new JTextArea();
        name.setWrapStyleWord(true);
        name.setLineWrap(true);
        name.setPreferredSize(new Dimension(80, 30));
        layout.putConstraint(SpringLayout.WEST, name, 20, SpringLayout.WEST, buttonPanel);
        layout.putConstraint(SpringLayout.NORTH, name, 5, SpringLayout.SOUTH, nameLabel);
        JLabel priceLabel = new JLabel("Ціна:");
        priceLabel.setPreferredSize(new Dimension( 80, 20));
        layout.putConstraint(SpringLayout.WEST, priceLabel, 20, SpringLayout.WEST, buttonPanel);
        layout.putConstraint(SpringLayout.NORTH, priceLabel, 5, SpringLayout.SOUTH, name);
        JTextArea price = new JTextArea();
        price.setWrapStyleWord(true);
        price.setLineWrap(true);
        price.setPreferredSize(new Dimension(80, 35));
        layout.putConstraint(SpringLayout.WEST, price, 20, SpringLayout.WEST, buttonPanel);
        layout.putConstraint(SpringLayout.NORTH, price, 5, SpringLayout.SOUTH, priceLabel);
        JLabel descriptionLabel = new JLabel("Опис:");
        descriptionLabel.setPreferredSize(new Dimension(140, 20));
        layout.putConstraint(SpringLayout.WEST, descriptionLabel, 20, SpringLayout.EAST, nameLabel);
        layout.putConstraint(SpringLayout.NORTH, descriptionLabel, 5, SpringLayout.SOUTH, delete);
        JTextArea changeDescription = new JTextArea();
        changeDescription.setLineWrap(true);
        changeDescription.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(changeDescription);
        descriptionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        descriptionScroll.setPreferredSize(new Dimension(140, 70));
        layout.putConstraint(SpringLayout.WEST, descriptionScroll, 20, SpringLayout.EAST, name);
        layout.putConstraint(SpringLayout.NORTH, descriptionScroll, 5, SpringLayout.SOUTH, descriptionLabel);
        JLabel producerLabel = new JLabel("Виробник:");
        producerLabel.setPreferredSize(new Dimension( 140, 20));
        layout.putConstraint(SpringLayout.WEST, producerLabel, 20, SpringLayout.EAST, descriptionLabel);
        layout.putConstraint(SpringLayout.NORTH, producerLabel, 5, SpringLayout.SOUTH, add);
        JTextArea producer = new JTextArea();
        producer.setLineWrap(true);
        producer.setWrapStyleWord(true);
        producer.setPreferredSize(new Dimension(140, 70));
        layout.putConstraint(SpringLayout.WEST, producer, 20, SpringLayout.EAST, descriptionScroll);
        layout.putConstraint(SpringLayout.NORTH, producer, 5, SpringLayout.SOUTH, nameLabel);
        JButton submit = new JButton("Підтвердити");
        submit.setPreferredSize(new Dimension(130, 20));
        layout.putConstraint(SpringLayout.WEST, submit, 20, SpringLayout.EAST, price);
        layout.putConstraint(SpringLayout.NORTH, submit, 10, SpringLayout.SOUTH, producer);
        JButton cancel = new JButton("Відмінити");
        cancel.setPreferredSize(new Dimension(130, 20));
        layout.putConstraint(SpringLayout.WEST, cancel, 30, SpringLayout.EAST, submit);
        layout.putConstraint(SpringLayout.NORTH, cancel, 10, SpringLayout.SOUTH, producer);
        buttonPanel.add(add);
        buttonPanel.add(changeGood);
        buttonPanel.add(delete);
        name.setLineWrap(true);
        name.setWrapStyleWord(true);
        ArrayList<JComponent> buttonPanelComponent = new ArrayList<>();
        buttonPanelComponent.add(name);
        buttonPanelComponent.add(nameLabel);
        buttonPanelComponent.add(descriptionLabel);
        buttonPanelComponent.add(producerLabel);
        buttonPanelComponent.add(producer);
        buttonPanelComponent.add(priceLabel);
        buttonPanelComponent.add(price);
        buttonPanelComponent.add(submit);
        buttonPanelComponent.add(cancel);
        buttonPanelComponent.add(descriptionScroll);
        buttonPanelComponent.forEach(buttonPanel::add);
        buttonPanelComponent.forEach(e -> e.setVisible(false));

        add.addActionListener(e -> {
            buttonPanelComponent.forEach(c -> c.setVisible(true));
            isChanges = 1;
        });
        changeGood.addActionListener(e -> {
            buttonPanelComponent.forEach(c -> c.setVisible(true));
            isChanges = 2;
        });
        delete.addActionListener(e -> {
            submit.setVisible(true);
            cancel.setVisible(true);
            isChanges = 3;
        });
        submit.addActionListener(e -> {
            if (isChanges == 1){
                if(name.getText().equals("") || changeDescription.getText().equals("") || producer.getText().equals("")
                || price.getText().equals("")){
                    name.setText("");
                    changeDescription.setText("");
                    producer.setText("");
                    price.setText("");
                    buttonPanelComponent.forEach(c -> c.setVisible(false));
                    JOptionPane.showMessageDialog(exceptionFrame,"Є пусті поля!");
                    return;
                }
                for (Goods g: storage.getAllGoods()){
                    if (g.getName().equals(name.getText())){
                        name.setText("");
                        changeDescription.setText("");
                        producer.setText("");
                        price.setText("");
                        buttonPanelComponent.forEach(c -> c.setVisible(false));
                        JOptionPane.showMessageDialog(exceptionFrame,"Товар з такою назвою вже інсує");
                        return;
                    }
                }
                try {
                    storage.getAllGroups().get(groupBox.getSelectedIndex())
                            .add(new Goods(name.getText(),changeDescription.getText(),
                                    producer.getText(),Double.parseDouble(price.getText())));
                } catch (NumberFormatException ex) {
                    name.setText("");
                    changeDescription.setText("");
                    producer.setText("");
                    price.setText("");
                    buttonPanelComponent.forEach(c -> c.setVisible(false));
                    JOptionPane.showMessageDialog(exceptionFrame,"Некоректна ціна!");
                    return;
                }
                Goods[] goods1 = new Goods[storage.getAllGroups().get(groupBox.getSelectedIndex()).getAll().size()];
                storage.getAllGroups().get(groupBox.getSelectedIndex()).getAll().toArray(goods1);
                goodsList.setListData(goods1);
                description.setText("");
            }
            if (isChanges == 2){
                if (goodsList.getSelectedIndex() == - 1){
                    name.setText("");
                    changeDescription.setText("");
                    producer.setText("");
                    price.setText("");
                    buttonPanelComponent.forEach(c -> c.setVisible(false));
                    JOptionPane.showMessageDialog(exceptionFrame,"Жоден алемент не вибрано!");
                    return;
                }
                try {
                    if (price.getText().equals(""))
                        storage.getAllGroups().get(groupBox.getSelectedIndex()).edit(goodsList.getSelectedValue().getName(),new Goods(name.getText(),changeDescription.getText(),
                                producer.getText(),0));
                    else
                        storage.getAllGroups().get(groupBox.getSelectedIndex()).edit(goodsList.getSelectedValue().getName(),new Goods(name.getText(),changeDescription.getText(),
                            producer.getText(),Double.parseDouble(price.getText())));
                } catch (NumberFormatException ex) {
                    name.setText("");
                    changeDescription.setText("");
                    producer.setText("");
                    price.setText("");
                    buttonPanelComponent.forEach(c -> c.setVisible(false));
                    JOptionPane.showMessageDialog(exceptionFrame,"Некоректна ціна!");
                    return;
                }
                Goods[] goods1 = new Goods[storage.getAllGroups().get(groupBox.getSelectedIndex()).getAll().size()];
                storage.getAllGroups().get(groupBox.getSelectedIndex()).getAll().toArray(goods1);
                goodsList.setListData(goods1);
                description.setText("");
            }
            if (isChanges == 3){
                if (goodsList.getSelectedIndex() == - 1){
                    buttonPanelComponent.forEach(c -> c.setVisible(false));
                    JOptionPane.showMessageDialog(exceptionFrame,"Жоден алемент не вибрано!");
                    return;
                }
                storage.getAllGroups().get(groupBox.getSelectedIndex()).delete(goodsList.getSelectedValue().getName());
                Goods[] goods1 = new Goods[storage.getAllGroups().get(groupBox.getSelectedIndex()).getAll().size()];
                storage.getAllGroups().get(groupBox.getSelectedIndex()).getAll().toArray(goods1);
                goodsList.setListData(goods1);
                description.setText("");
            }
            name.setText("");
            changeDescription.setText("");
            producer.setText("");
            price.setText("");
            buttonPanelComponent.forEach(c -> c.setVisible(false));
            changeGoodsPanel.setVisible(false);
            changeGoodsPanel.setVisible(true);
        });
        cancel.addActionListener(e -> {
            name.setText("");
            changeDescription.setText("");
            producer.setText("");
            price.setText("");
            buttonPanelComponent.forEach(c -> c.setVisible(false));
            changeGoodsPanel.setVisible(false);
            changeGoodsPanel.setVisible(true);
        });
        changeGoodsPanel.add(buttonPanelScroll);
        currentPanel = changeGoodsPanel;
        frame.add(changeGoodsPanel);
        frame.setVisible(false);
        frame.setVisible(true);
    }

    private static void search(){
        if (currentPanel != null){
            frame.remove(currentPanel);
        }
        JPanel searchPanel = new JPanel(new GridLayout(2,1));
        JPanel searchedData = new JPanel(new GridLayout(1,2));
        JList<Goods> searchedList = new JList<>();
        Goods[] goods = new Goods[storage.getAllGoods().size()];
        storage.getAllGoods().toArray(goods);
        searchedList.setListData(goods);
        JScrollPane searchedScroll = new JScrollPane(searchedList);
        searchedScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        searchedScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JTextArea searchedDescription = new JTextArea();
        searchedDescription.setWrapStyleWord(true);
        searchedDescription.setLineWrap(true);
        searchedDescription.setEditable(false);
        searchedData.add(searchedScroll);
        searchedList.addListSelectionListener(e -> {
            if (searchedList.getSelectedValue() != null)
                for (Group g:storage.getAllGroups())
                    for (Goods good:g.getAll())
                        if (good.getName().equals(searchedList.getSelectedValue().getName())
                                && !good.getName().equals("Нічого не знайдено"))
                            searchedDescription.setText("Група: " + g.getName()
                                    + "\n" + "Опис: " + searchedList.getSelectedValue().getDescription()
                                    + "\n" + "Виробник: " + searchedList.getSelectedValue().getProducer()
                                    + "\n" + "Ціна за одиницю: " + searchedList.getSelectedValue().getPrice());
        });
        searchedList.setSelectedIndex(0);
        JScrollPane searchedDescriptionScroll = new JScrollPane(searchedDescription);
        searchedDescriptionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        searchedDescriptionScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        searchedData.add(searchedDescriptionScroll);
        searchPanel.add(searchedData);
        SpringLayout layout = new SpringLayout();
        JPanel searchButtonPanel = new JPanel(layout);

        JLabel nameLabel = new JLabel("Назва:");
        nameLabel.setPreferredSize(new Dimension( 100, 20));
        layout.putConstraint(SpringLayout.WEST, nameLabel, 20, SpringLayout.WEST, searchButtonPanel);
        layout.putConstraint(SpringLayout.NORTH, nameLabel, 5, SpringLayout.NORTH, searchButtonPanel);
        JTextArea name = new JTextArea();
        name.setWrapStyleWord(true);
        name.setLineWrap(true);
        name.setPreferredSize(new Dimension(100, 30));
        layout.putConstraint(SpringLayout.WEST, name, 20, SpringLayout.WEST, searchButtonPanel);
        layout.putConstraint(SpringLayout.NORTH, name, 5, SpringLayout.SOUTH, nameLabel);

        JLabel producerLabel = new JLabel("Виробник:");
        producerLabel.setPreferredSize(new Dimension( 100, 20));
        layout.putConstraint(SpringLayout.WEST, producerLabel, 20, SpringLayout.WEST, searchButtonPanel);
        layout.putConstraint(SpringLayout.NORTH, producerLabel, 5, SpringLayout.SOUTH, name);
        JTextArea producer = new JTextArea();
        producer.setLineWrap(true);
        producer.setWrapStyleWord(true);
        producer.setPreferredSize(new Dimension(100, 30));
        layout.putConstraint(SpringLayout.WEST, producer, 20, SpringLayout.WEST, searchButtonPanel);
        layout.putConstraint(SpringLayout.NORTH, producer, 5, SpringLayout.SOUTH, producerLabel);

        JLabel descriptionLabel = new JLabel("Опис:");
        descriptionLabel.setPreferredSize(new Dimension(110, 20));
        layout.putConstraint(SpringLayout.WEST, descriptionLabel, 20, SpringLayout.EAST, nameLabel);
        layout.putConstraint(SpringLayout.NORTH, descriptionLabel, 5, SpringLayout.NORTH, searchButtonPanel);

        JTextArea changeDescription = new JTextArea();
        changeDescription.setLineWrap(true);
        changeDescription.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(changeDescription);
        descriptionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        descriptionScroll.setPreferredSize(new Dimension(110, 80));
        layout.putConstraint(SpringLayout.WEST, descriptionScroll, 20, SpringLayout.EAST, name);
        layout.putConstraint(SpringLayout.NORTH, descriptionScroll, 5, SpringLayout.SOUTH, descriptionLabel);

        JComboBox<String> groupComboBox = new JComboBox<>();
        groupComboBox.addItem("Всі групи");
        storage.getAllGroups().stream().map(Group::getName).forEach(groupComboBox::addItem);
        groupComboBox.setSelectedIndex(0);
        groupComboBox.setPreferredSize(new Dimension(150, 20));
        layout.putConstraint(SpringLayout.WEST, groupComboBox, 20, SpringLayout.EAST, descriptionLabel);
        layout.putConstraint(SpringLayout.NORTH, groupComboBox, 10, SpringLayout.NORTH, searchButtonPanel);

        JLabel minLabel = new JLabel("Мінімальна ціна");
        minLabel.setPreferredSize(new Dimension(130, 15));
        layout.putConstraint(SpringLayout.WEST, minLabel, 20, SpringLayout.EAST, descriptionLabel);
        layout.putConstraint(SpringLayout.NORTH, minLabel, 10, SpringLayout.SOUTH, groupComboBox);

        JSlider min = new JSlider(JSlider.HORIZONTAL,(int) storage.getAllGoods().stream().mapToDouble(Goods::getPrice).min().getAsDouble(),
                (int) storage.getAllGoods().stream().mapToDouble(Goods::getPrice).max().getAsDouble() + 1,
                (int) storage.getAllGoods().stream().mapToDouble(Goods::getPrice).min().getAsDouble());
        min.setPreferredSize(new Dimension(200, 40));
        min.setMinorTickSpacing(min.getMaximum() / 6);
        min.setMajorTickSpacing(min.getMaximum() / 3);
        min.setPaintLabels(true);
        min.setPaintTrack(true);
        layout.putConstraint(SpringLayout.WEST, min, 20, SpringLayout.EAST, descriptionLabel);
        layout.putConstraint(SpringLayout.NORTH, min, 10, SpringLayout.SOUTH, minLabel);

        JLabel maxLabel = new JLabel("Максимальна ціна");
        maxLabel.setPreferredSize(new Dimension(130, 15));
        layout.putConstraint(SpringLayout.WEST, maxLabel, 20, SpringLayout.EAST, descriptionLabel);
        layout.putConstraint(SpringLayout.NORTH, maxLabel, 10, SpringLayout.SOUTH, min);

        JSlider max = new JSlider(JSlider.HORIZONTAL,(int) storage.getAllGoods().stream().mapToDouble(Goods::getPrice).min().getAsDouble(),
                (int) storage.getAllGoods().stream().mapToDouble(Goods::getPrice).max().getAsDouble() + 1,
                (int) storage.getAllGoods().stream().mapToDouble(Goods::getPrice).max().getAsDouble() + 1);
        max.setMinorTickSpacing(min.getMaximum() / 6);
        max.setMajorTickSpacing(min.getMaximum() / 3);
        max.setPaintLabels(true);
        max.setPaintTrack(true);
        max.setPreferredSize(new Dimension(200, 40));
        layout.putConstraint(SpringLayout.WEST, max, 20, SpringLayout.EAST, descriptionLabel);
        layout.putConstraint(SpringLayout.NORTH, max, 10, SpringLayout.SOUTH, maxLabel);

        JButton search = new JButton("Пошук");
        search.setPreferredSize(new Dimension(130, 20));
        layout.putConstraint(SpringLayout.WEST, search, 60, SpringLayout.WEST, searchButtonPanel);
        layout.putConstraint(SpringLayout.NORTH, search, 20, SpringLayout.SOUTH, producer);
        search.addActionListener(e -> {
            ArrayList<Goods> filteredGoods;
            if (groupComboBox.getSelectedIndex() == 0)
                filteredGoods = storage.getAllGoods();
            else filteredGoods = storage.getAllGroups().get(groupComboBox.getSelectedIndex() - 1).getAll();
            if (!name.getText().equals(""))
                filteredGoods = (ArrayList<Goods>) filteredGoods.stream()
                        .filter(f -> f.getName().toLowerCase(Locale.ROOT).matches("\\D*" + name.getText().toLowerCase(Locale.ROOT) + "\\D*"))
                        .collect(Collectors.toList());
            if (!changeDescription.getText().equals(""))
                filteredGoods = (ArrayList<Goods>) filteredGoods.stream().filter(f -> {
                    String[] s = changeDescription.getText().toLowerCase(Locale.ROOT).split(" ");
                    for (String str:s)
                        if (f.getDescription().toLowerCase(Locale.ROOT).matches(".*" + str + ".*"))
                            return true;
                    return false;
                }).collect(Collectors.toList());
            if (!producer.getText().equals(""))
                filteredGoods = (ArrayList<Goods>) filteredGoods.stream()
                        .filter(f -> f.getProducer().toLowerCase(Locale.ROOT).matches("\\D*" + producer.getText().toLowerCase(Locale.ROOT)  + "\\D*"))
                        .collect(Collectors.toList());
            if (min.getValue() > max.getValue()){
                JOptionPane.showMessageDialog(exceptionFrame,"Мінімальна ціна більша за максимальну!");
                return;
            }
            filteredGoods = (ArrayList<Goods>) filteredGoods.stream()
                    .filter(f -> f.getPrice() >= min.getValue() && f.getPrice() <= max.getValue())
                    .collect(Collectors.toList());
            if (filteredGoods.size() == 0)
                filteredGoods.add(new Goods("Нічого не знайдено","","",0));
            Goods[] goods1 = new Goods[filteredGoods.size()];
            filteredGoods.toArray(goods1);
            searchedList.setListData(goods1);
            searchedDescription.setText("");
        });

        searchButtonPanel.add(nameLabel);
        searchButtonPanel.add(name);
        searchButtonPanel.add(descriptionLabel);
        searchButtonPanel.add(descriptionScroll);
        searchButtonPanel.add(groupComboBox);
        searchButtonPanel.add(producerLabel);
        searchButtonPanel.add(producer);
        searchButtonPanel.add(minLabel);
        searchButtonPanel.add(min);
        searchButtonPanel.add(maxLabel);
        searchButtonPanel.add(max);
        searchButtonPanel.add(search);
        searchButtonPanel.setPreferredSize(new Dimension(450,185));
        JScrollPane buttonPanelScroll = new JScrollPane(searchButtonPanel);
        buttonPanelScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        buttonPanelScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        searchPanel.add(buttonPanelScroll);
        currentPanel = searchPanel;
        frame.add(searchPanel);
        frame.setVisible(false);
        frame.setVisible(true);
    }

    private static void getInformationOnStorage(){
        if (currentPanel != null){
            frame.remove(currentPanel);
        }
        JPanel getInformationOnStoragePanel = new JPanel(new GridLayout(1, 2));
        JList<Goods> goodsJList = new JList<>();
        Goods[] goods = new Goods[storage.getAllGoods().size()];
        storage.getAllGoods().toArray(goods);
        goodsJList.setListData(goods);
        goodsJList.setSelectedIndex(0);
        JScrollPane listScroll = new JScrollPane(goodsJList);
        listScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        getInformationOnStoragePanel.add(listScroll);

        JTextArea description = new JTextArea();
        description.setEditable(false);
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        JScrollPane descriptionScroll = new JScrollPane(description);
        descriptionScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        descriptionScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        getInformationOnStoragePanel.add(descriptionScroll);
        goodsJList.addListSelectionListener(e -> {
            if (goodsJList.getSelectedValue() != null)
                for (Group g:storage.getAllGroups())
                    for (Goods good:g.getAll())
                        if (good.getName().equals(goodsJList.getSelectedValue().getName()))
                            description.setText("Група: " + g.getName()
                                    + "\n" + "Опис: " + goodsJList.getSelectedValue().getDescription()
                                    + "\n" + "Виробник: " + goodsJList.getSelectedValue().getProducer()
                                    + "\n" + "Кількість на складі: " + goodsJList.getSelectedValue().getCount()
                                    + "\n" + "Ціна за одиницю: " + goodsJList.getSelectedValue().getPrice());
        });
        currentPanel = getInformationOnStoragePanel;
        frame.add(getInformationOnStoragePanel);
        frame.setVisible(false);
        frame.setVisible(true);
    }

    private static void getInformationByGroup(){
        if (currentPanel != null){
            frame.remove(currentPanel);
        }
        JPanel getInfoByGroupPanel = new JPanel(new GridLayout(1, 2));
        JList<Goods> goodsJList = new JList<>();
        Goods[] goods = new Goods[storage.getAllGroups().get(0).getAll().size()];
        storage.getAllGroups().get(0).getAll().toArray(goods);
        goodsJList.setListData(goods);
        goodsJList.setSelectedIndex(0);
        JScrollPane listScroll = new JScrollPane(goodsJList);
        listScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        getInfoByGroupPanel.add(listScroll);
        SpringLayout springLayout = new SpringLayout();
        JPanel goodDescriptionPanel = new JPanel(springLayout);
        JComboBox<String> groupBox = new JComboBox<String>();
        storage.getAllGroups().stream().map(Group::getName).forEach(groupBox::addItem);
        groupBox.setSelectedIndex(0);
        groupBox.setPreferredSize(new Dimension(230,20));
        springLayout.putConstraint(SpringLayout.WEST, groupBox, 5, SpringLayout.WEST, goodDescriptionPanel);
        springLayout.putConstraint(SpringLayout.NORTH, groupBox, 5, SpringLayout.NORTH, goodDescriptionPanel);
        groupBox.addActionListener(e -> {
            if (e.getActionCommand().equals("comboBoxChanged")){
                Goods[] goods1 = new Goods[storage.getAllGroups().get(groupBox.getSelectedIndex()).getAll().size()];
                storage.getAllGroups().get(groupBox.getSelectedIndex()).getAll().toArray(goods1);
                goodsJList.setListData(goods1);
            }
        });
        goodDescriptionPanel.add(groupBox);
        JTextArea description = new JTextArea();
        description.setPreferredSize(new Dimension(230, 155));
        springLayout.putConstraint(SpringLayout.WEST, description, 5, SpringLayout.WEST, goodDescriptionPanel);
        springLayout.putConstraint(SpringLayout.NORTH, description, 5, SpringLayout.SOUTH, groupBox);
        goodDescriptionPanel.add(description);
        description.setEditable(false);
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        goodsJList.addListSelectionListener(e -> {
            if (goodsJList.getSelectedValue() != null)
                for (Group g:storage.getAllGroups())
                    for (Goods good:g.getAll())
                        if (good.getName().equals(goodsJList.getSelectedValue().getName()))
                            description.setText("Опис: " + goodsJList.getSelectedValue().getDescription()
                                    + "\n" + "Виробник: " + goodsJList.getSelectedValue().getProducer()
                                    + "\n" + "Кількість на складі: " + goodsJList.getSelectedValue().getCount()
                                    + "\n" + "Ціна за одиницю: " + goodsJList.getSelectedValue().getPrice());
        });
        goodDescriptionPanel.setPreferredSize(new Dimension(210,190));
        JScrollPane goodDescriptionScroll = new JScrollPane(goodDescriptionPanel);
        goodDescriptionScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        goodDescriptionScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        getInfoByGroupPanel.add(goodDescriptionScroll);
        currentPanel = getInfoByGroupPanel;
        frame.add(currentPanel);
        frame.setVisible(false);
        frame.setVisible(true);
    }

    private static void allGoodsCostInformation(){
        if (currentPanel != null){
            frame.remove(currentPanel);
        }
        JPanel panel = new JPanel(new GridLayout(1,1));
        JList<String> info = new JList<>();
        String[] goods = new String[storage.getAllGoods().size() + 1];
        List<String> collect = storage.getAllGoods().stream()
                .map(e -> e.getName() + " - " + e.getPrice() * e.getCount() + " грн")
                .collect(Collectors.toList());
        collect.add("Усього: " + storage.getAllGoods().stream().mapToDouble(e -> e.getCount() * e.getPrice()).sum() + " грн");
        collect.toArray(goods);
        info.setListData(goods);
        JScrollPane infoScroll = new JScrollPane(info);
        infoScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        infoScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(infoScroll);
        frame.add(panel);
        currentPanel = panel;
        frame.setVisible(false);
        frame.setVisible(true);
    }

    private static void allGoodsCostInformationByGroup(){
        if (currentPanel != null){
            frame.remove(currentPanel);
        }
        JPanel panel = new JPanel(new GridLayout(1,1));
        JList<String> info = new JList<>();
        String[] goods = new String[storage.getAllGroups().size() + 1];
        List<String> collect = storage.getAllGroups().stream()
                .map(e -> e.getName() + " - " + e.getAll().stream().mapToDouble(p -> p.getPrice() * p.getCount()).sum() + " грн")
                .collect(Collectors.toList());
        collect.add("Усього: " + storage.getAllGoods().stream().mapToDouble(e -> e.getCount() * e.getPrice()).sum() + " грн");
        collect.toArray(goods);
        info.setListData(goods);
        JScrollPane infoScroll = new JScrollPane(info);
        infoScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        infoScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(infoScroll);
        frame.add(panel);
        currentPanel = panel;
        frame.setVisible(false);
        frame.setVisible(true);
    }

    private static void addGoods(){
        if (currentPanel != null){
            frame.remove(currentPanel);
        }
        JPanel panel = new JPanel(new GridLayout(1, 2));
        JList<String> goodsJList = new JList<String>();
        String[] objects = new String[storage.getAllGoods().size()];
        storage.getAllGoods().stream().map(e -> e.getName() + " - кількість: " + e.getCount()).collect(Collectors.toList()).toArray(objects);
        goodsJList.setListData(objects);
        goodsJList.setSelectedIndex(0);
        JScrollPane listScroll = new JScrollPane(goodsJList);
        listScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(listScroll);

        SpringLayout layout = new SpringLayout();
        JPanel addGoodsPanel = new JPanel(layout);
        JLabel numberLabel = new JLabel("Кількість товару:");
        numberLabel.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, numberLabel, 10, SpringLayout.WEST, addGoodsPanel);
        layout.putConstraint(SpringLayout.NORTH, numberLabel, 10, SpringLayout.NORTH, addGoodsPanel);
        JTextField numberField = new JTextField();
        numberField.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, numberField, 5, SpringLayout.WEST, addGoodsPanel);
        layout.putConstraint(SpringLayout.NORTH, numberField, 5, SpringLayout.SOUTH, numberLabel);
        JButton button = new JButton("Додати");
        button.addActionListener(e -> {
            if (goodsJList.getSelectedIndex() == - 1){
                numberField.setText("");
                JOptionPane.showMessageDialog(exceptionFrame,"Жоден товар не вибрано");
                return;
            }
            try {
                storage.getAllGoods().get(goodsJList.getSelectedIndex()).increaseCount(Integer.parseInt(numberField.getText()));
            } catch (NumberFormatException ex){
                numberField.setText("");
                JOptionPane.showMessageDialog(exceptionFrame,"Введено некоректне число");
                return;
            } catch (RuntimeException ex){
                numberField.setText("");
                JOptionPane.showMessageDialog(exceptionFrame,ex.getMessage());
                return;
            }
            numberField.setText("");
            String[] objects1 = new String[storage.getAllGoods().size()];
            storage.getAllGoods().stream().map(f -> f.getName() + " - кількість: " + f.getCount()).collect(Collectors.toList()).toArray(objects1);
            goodsJList.setListData(objects1);
        });
        button.setPreferredSize(new Dimension(100, 20));
        layout.putConstraint(SpringLayout.WEST, button, 50, SpringLayout.WEST, addGoodsPanel);
        layout.putConstraint(SpringLayout.NORTH, button, 10, SpringLayout.SOUTH, numberField);
        addGoodsPanel.add(numberField);
        addGoodsPanel.add(numberLabel);
        addGoodsPanel.add(button);
        panel.add(addGoodsPanel);
        addGoodsPanel.setPreferredSize(new Dimension(210,370));
        JScrollPane addGoodsScroll = new JScrollPane(addGoodsPanel);
        addGoodsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        addGoodsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(addGoodsScroll);
        frame.add(panel);
        currentPanel = panel;
        frame.setVisible(false);
        frame.setVisible(true);
    }

    private static void removeGoods(){
        if (currentPanel != null){
            frame.remove(currentPanel);
        }
        JPanel panel = new JPanel(new GridLayout(1, 2));
        JList<String> goodsJList = new JList<String>();
        String[] objects = new String[storage.getAllGoods().size()];
        storage.getAllGoods().stream().map(e -> e.getName() + " - кількість: " + e.getCount()).toList().toArray(objects);
        goodsJList.setListData(objects);
        goodsJList.setSelectedIndex(0);
        JScrollPane listScroll = new JScrollPane(goodsJList);
        listScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        listScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(listScroll);

        SpringLayout layout = new SpringLayout();
        JPanel addGoodsPanel = new JPanel(layout);
        JLabel numberLabel = new JLabel("Кількість товару:");
        numberLabel.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, numberLabel, 10, SpringLayout.WEST, addGoodsPanel);
        layout.putConstraint(SpringLayout.NORTH, numberLabel, 10, SpringLayout.NORTH, addGoodsPanel);
        JTextField numberField = new JTextField();
        numberField.setPreferredSize(new Dimension(200, 20));
        layout.putConstraint(SpringLayout.WEST, numberField, 5, SpringLayout.WEST, addGoodsPanel);
        layout.putConstraint(SpringLayout.NORTH, numberField, 5, SpringLayout.SOUTH, numberLabel);
        JButton button = new JButton("Списати");
        button.addActionListener(e -> {
            if (goodsJList.getSelectedIndex() == - 1){
                numberField.setText("");
                JOptionPane.showMessageDialog(exceptionFrame,"Жоден товар не вибрано");
                return;
            }
            try {
                storage.getAllGoods().get(goodsJList.getSelectedIndex()).decreaseCount(Integer.parseInt(numberField.getText()));
            } catch (NumberFormatException ex){
                numberField.setText("");
                JOptionPane.showMessageDialog(exceptionFrame,"Введено некоректне число");
                return;
            } catch (RuntimeException ex){
                numberField.setText("");
                JOptionPane.showMessageDialog(exceptionFrame,ex.getMessage());
                return;
            }
            numberField.setText("");
            String[] objects1 = new String[storage.getAllGoods().size()];
            storage.getAllGoods().stream().map(f -> f.getName() + " - кількість: " + f.getCount()).collect(Collectors.toList()).toArray(objects1);
            goodsJList.setListData(objects1);
        });
        button.setPreferredSize(new Dimension(100, 20));
        layout.putConstraint(SpringLayout.WEST, button, 50, SpringLayout.WEST, addGoodsPanel);
        layout.putConstraint(SpringLayout.NORTH, button, 10, SpringLayout.SOUTH, numberField);
        addGoodsPanel.add(numberField);
        addGoodsPanel.add(numberLabel);
        addGoodsPanel.add(button);
        panel.add(addGoodsPanel);
        addGoodsPanel.setPreferredSize(new Dimension(210,370));
        JScrollPane addGoodsScroll = new JScrollPane(addGoodsPanel);
        addGoodsScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        addGoodsScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(addGoodsScroll);
        frame.add(panel);
        currentPanel = panel;
        frame.setVisible(false);
        frame.setVisible(true);
    }
}