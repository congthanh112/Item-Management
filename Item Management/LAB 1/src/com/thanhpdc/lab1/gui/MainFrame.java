package com.thanhpdc.lab1.gui;

import com.thanhpdc.lab1.dao.ItemDAO;
import com.thanhpdc.lab1.dao.SupplierDAO;
import com.thanhpdc.lab1.dto.ItemDTO;
import com.thanhpdc.lab1.dto.SupplierDTO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends javax.swing.JDialog {

    LoginFrame loginFormm;
    ArrayList<SupplierDTO> listSupplier;
    ArrayList<ItemDTO> listItem;
    DefaultComboBoxModel defaultComboBoxModel;
    String regex = "^[a-zA-Z0-9\\s]+$";
    int index = -1;
    boolean flagSupplierAddNew = false;                    //TRUE sau khi bấm Add New lần 1, false sau khi bấm lần 2 bất kể Save hay không
    boolean flagItemAddNew = false;                       //TRUE sau khi bấm Add New lần 1, false sau khi bấm lần 2 bất kể Save hay không
    boolean flagSupplierSave = false;                     //TRUE sau khi đã Save thành công, FALSE khi chưa Save
    boolean flagItemSave = false;                         //TRUE sau khi đã Save thành công, FALSE khi chưa Save
    //boolean flagSupplierPanel = false;
    boolean flagSupplierPanel = true;    //Phải đặt ngược lại
    boolean flagItemPanel = false;        // tại vì mới vô cái sự kiện TabChange nó đổi lại

    public MainFrame(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setTitle("Manager");
        this.setLocationRelativeTo(null);
        if (!parent.isActive()) {
            JOptionPane.showMessageDialog(null, "Please login before using");
            System.exit(0);
        } else {
            loginFormm = (LoginFrame) parent;
            lblWelcome.setText("Welcome " + LoginFrame.name);
            loginFormm.setVisible(false);
        }
        try {
            loadSupplier();
            resetSupplierPanel();
            loadItem();
            resetItemPanel();
        } catch (Exception e) {
        }
    }

    public void loadSupplier() {
        try {
            listSupplier = SupplierDAO.getSupplier();
            DefaultTableModel supplierModel = (DefaultTableModel) tblSupplier.getModel();
            supplierModel.getDataVector().removeAllElements();
            for (SupplierDTO suppliers : listSupplier) {
                Vector supplierRow = new Vector();
                supplierRow.add(suppliers.getSupCode());
                supplierRow.add(suppliers.getSupName());
                supplierRow.add(suppliers.getAddress());
                supplierModel.addRow(supplierRow);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Loading Supplier error");
        }
    }

    public void loadItem() throws SQLException {
        listItem = ItemDAO.getItem();
        DefaultTableModel itemModel = (DefaultTableModel) tblItem.getModel();
        itemModel.getDataVector().removeAllElements();
        defaultComboBoxModel = new DefaultComboBoxModel();
        defaultComboBoxModel.removeAllElements();
        defaultComboBoxModel.addElement("--Seletion Supplier--");
        try {
            for (SupplierDTO supplier : listSupplier) {
                defaultComboBoxModel.addElement(supplier.getSupCode() + " - " + supplier.getSupName());
            }
            cbxItemSupplier.setModel(defaultComboBoxModel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Supplier loading error");
        }
        try {
            listItem = ItemDAO.getItem();
            for (ItemDTO item : listItem) {
                Vector itemRow = new Vector();
                itemRow.add(item.getItemCode());
                itemRow.add(item.getItemName());
                itemRow.add(item.getItemSupCode() + " - " + item.getItemSupName());
                itemRow.add(item.getUnit());
                itemRow.add(item.getPrice());
                itemRow.add(item.isSupplying());
                itemModel.addRow(itemRow);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Item loading error ");
        }
    }

    public boolean testInputSupplier() {
        clearNotification();
        boolean result = true;
        if (txtSupCode.getText().isEmpty() || txtSupName.getText().isEmpty() || txtAddress.getText().isEmpty()) {
            if (txtSupCode.getText().isEmpty()) {
                lblSupCodeWarnning.setText("Please input supplier code  ");
            }
            if (txtSupName.getText().isEmpty()) {
                lblSupNameWarnning.setText("Please input supplier name  ");
            }
            if (txtAddress.getText().isEmpty()) {
                lblAddressWarnning.setText("Please input supplier address  ");
            }
            result = false;
        } else {
            if (!txtSupCode.getText().matches(regex) || !txtSupName.getText().matches(regex) || !txtAddress.getText().matches(regex)) {
                if (txtSupCode.getText().matches(regex)) {
                    lblSupCodeWarnning.setText("Supplier code is invalid  ");
                }
                if (txtSupName.getText().isEmpty()) {
                    lblSupNameWarnning.setText("Supplier name is invalid  ");
                }
                if (txtAddress.getText().isEmpty()) {
                    lblAddressWarnning.setText("Supplier address is invalid  ");
                }
                result = false;
            }
        }
        return result;
    }

    public boolean testInputItem() {
        boolean result = true;
        clearNotification();
        if (txtItemCode.getText().isEmpty() || txtItemName.getText().isEmpty() || cbxItemSupplier.getSelectedIndex() == 0 || txtItemUnit.getText().isEmpty() || txtItemPrice.getText().isEmpty()) {
            if (txtItemCode.getText().isEmpty()) {
                lblItemCodeWarnning.setText("Please input item code  ");
            }
            if (txtItemName.getText().isEmpty()) {
                lblItemNameWarnning.setText("Please input item name  ");
            }
            if (cbxItemSupplier.getSelectedIndex() == 0) {
                lblItemSupplierWarnning.setText("Please choose supplier  ");
            }
            if (txtItemUnit.getText().isEmpty()) {
                lblItemUnitWarnning.setText("Please input item unit  ");
            }
            if (txtItemPrice.getText().isEmpty()) {
                lblItemPriceWarnning.setText("Please input item price  ");
            }
            result = false;
        } else {
            if (!txtItemCode.getText().matches(regex) || !txtItemName.getText().matches(regex) || cbxItemSupplier.getSelectedIndex() == 0 || !txtItemUnit.getText().matches(regex) || !txtItemPrice.getText().matches("^[0-9.,]{1,10}$")) {
                if (!txtItemCode.getText().matches(regex)) {
                    lblItemCodeWarnning.setText("Item code is invalid  ");
                }
                if (!txtItemName.getText().matches(regex)) {
                    lblItemNameWarnning.setText("Item name is invalid  ");
                }
                if (cbxItemSupplier.getSelectedIndex() == 0) {
                    lblItemSupplierWarnning.setText("Please choose Supplier  ");
                }
                if (!txtItemUnit.getText().matches(regex)) {
                    lblItemUnitWarnning.setText("Item unit is invalid  ");
                }
                try {
                    float price = Float.parseFloat(txtItemPrice.getText());
                    if (price <= 0) {
                        lblItemPriceWarnning.setText("Price must be greater than 0  ");
                    }
                } catch (Exception e) {
                    lblItemPriceWarnning.setText("Item price is invalid  ");
                }
                result = false;
            }
        }
        return result;
    }

    public boolean checkInputSupplier() {
        clearNotification();
        boolean result = true;
        if (txtSupCode.getText().isEmpty() || txtSupName.getText().isEmpty() || txtAddress.getText().isEmpty()) {
            result = false;
        } else {
            if (!txtSupCode.getText().matches(regex) || !txtSupName.getText().matches(regex) || !txtAddress.getText().matches(regex)) {
                result = false;
            }
        }
        return result;
    }

    public void resetSupplierPanel() {
        index = -1;
        txtSupCode.setText("");
        txtSupName.setText("");
        txtAddress.setText("");
        ckbCollaborating.setSelected(false);

        txtSupCode.setEditable(false);
        txtSupName.setEditable(false);
        txtAddress.setEditable(false);
        ckbCollaborating.setEnabled(false);

        lblSupCodeWarnning.setText("");
        lblSupNameWarnning.setText("");
        lblAddressWarnning.setText("");
        lblSupplierNotification.setText("");

        btnSupplierAddNew.setEnabled(true);
        btnSupplierSave.setEnabled(false);
        btnSupplierDelete.setEnabled(false);

        tblSupplier.clearSelection();
        tblSupplier.setEnabled(true);
    }

    public boolean checkInputItem() {
        boolean result = true;
        clearNotification();
        if (txtItemCode.getText().isEmpty() || txtItemName.getText().isEmpty() || cbxItemSupplier.getSelectedIndex() == 0 || txtItemUnit.getText().isEmpty() || txtItemPrice.getText().isEmpty()) {
            result = false;
        } else {
            if (!txtItemCode.getText().matches(regex) || !txtItemName.getText().matches(regex) || cbxItemSupplier.getSelectedIndex() == 0 || !txtItemUnit.getText().matches(regex) || !txtItemPrice.getText().matches("^[0-9.,]{1,10}$")) {

                try {
                    float price = Float.parseFloat(txtItemPrice.getText());
                    if (price <= 0) {
                        lblItemPriceWarnning.setText("Price must be greater than 0  ");
                    }
                } catch (Exception e) {
                    result = false;
                }
                result = false;
            }
        }
        return result;
    }

    public void resetItemPanel() {
        index = -1;
        txtItemCode.setText("");
        txtItemName.setText("");
        cbxItemSupplier.setSelectedIndex(0);
        txtItemUnit.setText("");
        txtItemPrice.setText("");
        ckbItemSupplying.setSelected(false);

        txtItemCode.setEditable(false);
        txtItemName.setEditable(false);
        cbxItemSupplier.setEnabled(false);
        txtItemUnit.setEditable(false);
        txtItemPrice.setEditable(false);
        ckbItemSupplying.setEnabled(false);

        lblItemNameWarnning.setText("");
        lblItemNameWarnning.setText("");
        lblItemSupplierWarnning.setText("");
        lblItemUnitWarnning.setText("");
        lblItemPriceWarnning.setText("");
        lblItemNotification.setText("");

        btnItemAddNew.setEnabled(true);
        btnItemSave.setEnabled(false);
        btnItemDelete.setEnabled(false);

        tblItem.clearSelection();
        tblSupplier.setEnabled(true);
    }

    public void clearText() {
        txtSupCode.setText("");
        txtSupName.setText("");
        txtAddress.setText("");
        ckbCollaborating.setSelected(false);

        txtItemCode.setText("");
        txtItemName.setText("");
        cbxItemSupplier.setSelectedIndex(0);
        txtItemUnit.setText("");
        txtItemPrice.setText("");
        ckbItemSupplying.setSelected(false);
    }

    public void clearNotification() {
        lblSupCodeWarnning.setText("");
        lblSupNameWarnning.setText("");
        lblAddressWarnning.setText("");
        lblSupplierNotification.setText("");

        lblItemCodeWarnning.setText("");
        lblItemNameWarnning.setText("");
        lblItemSupplierWarnning.setText("");
        lblItemUnitWarnning.setText("");
        lblItemPriceWarnning.setText("");
        lblItemNotification.setText("");
    }

    public boolean checkSupplierUpdate() {        // có sửa thằng nào đó thì trả về FALSE, không có sửa thì trả về TRUE
        int index = tblSupplier.getSelectedRow();
        if (index >= 0) {
            String supCodeCheck = txtSupCode.getText();
            String supNameCheck = txtSupName.getText();
            String addressCheck = txtAddress.getText();
            boolean collaboratingCheck = ckbCollaborating.isSelected();
            for (int i = 0; i < listSupplier.size(); i++) {
                if (supCodeCheck.equals(listSupplier.get(i).getSupCode())) {
                    if (!supNameCheck.equals(listSupplier.get(i).getSupName())
                            || !addressCheck.equals(listSupplier.get(i).getAddress())
                            || collaboratingCheck != listSupplier.get(i).isCollaborating()) {
                        return false;
                    }
                }

            }
        }
        return true;
    }

    public boolean checkItemUpdate() {             // có sửa thằng nào đó thì trả về FALSE, không có sửa thì trả về TRUE
        int index = tblItem.getSelectedRow();
        if (index >= 0) {
            String itemCodeCheck = txtItemCode.getText();
            String itemNameCheck = txtItemName.getText();
            String supplierCheck = cbxItemSupplier.getSelectedItem().toString();
            String unitCheck = txtItemUnit.getText();
            float priceCheck = Float.parseFloat(txtItemPrice.getText());
            boolean supplyingCheck = ckbItemSupplying.isSelected();
            for (int i = 0; i < listItem.size(); i++) {
                if (itemCodeCheck.equals(listItem.get(i).getItemCode())) {
                    if (!itemNameCheck.equals(listItem.get(i).getItemName())
                            || !supplierCheck.equals(listItem.get(i).getItemSupCode() + " - " + listItem.get(i).getItemSupName())
                            || !unitCheck.equals(listItem.get(i).getUnit())
                            || priceCheck != listItem.get(i).getPrice()
                            || supplyingCheck != listItem.get(i).isSupplying()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblWelcome = new javax.swing.JLabel();
        btnLogOut = new javax.swing.JButton();
        myTab = new javax.swing.JTabbedPane();
        SupplierPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSupplier = new javax.swing.JTable();
        lblSupplierCode = new javax.swing.JLabel();
        lblSupplierName = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblCollaborating = new javax.swing.JLabel();
        btnSupplierAddNew = new javax.swing.JButton();
        btnSupplierSave = new javax.swing.JButton();
        btnSupplierDelete = new javax.swing.JButton();
        txtSupCode = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtSupName = new javax.swing.JTextField();
        ckbCollaborating = new javax.swing.JCheckBox();
        lblSupCodeWarnning = new javax.swing.JLabel();
        lblSupNameWarnning = new javax.swing.JLabel();
        lblAddressWarnning = new javax.swing.JLabel();
        lblSupplierNotification = new javax.swing.JLabel();
        ItemPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblItem = new javax.swing.JTable();
        btnItemAddNew = new javax.swing.JButton();
        btnItemSave = new javax.swing.JButton();
        btnItemDelete = new javax.swing.JButton();
        lblItemCode = new javax.swing.JLabel();
        lblItemName = new javax.swing.JLabel();
        cbxItemSupplier = new javax.swing.JComboBox<>();
        lblItemSupplier = new javax.swing.JLabel();
        lblItemUnit = new javax.swing.JLabel();
        lblItemPrice = new javax.swing.JLabel();
        lblItemSupplying = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        txtItemName = new javax.swing.JTextField();
        txtItemUnit = new javax.swing.JTextField();
        txtItemPrice = new javax.swing.JTextField();
        ckbItemSupplying = new javax.swing.JCheckBox();
        lblItemNameWarnning = new javax.swing.JLabel();
        lblItemSupplierWarnning = new javax.swing.JLabel();
        lblItemUnitWarnning = new javax.swing.JLabel();
        lblItemCodeWarnning = new javax.swing.JLabel();
        lblItemPriceWarnning = new javax.swing.JLabel();
        lblItemNotification = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lblWelcome.setFont(new java.awt.Font("Script MT Bold", 0, 75)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(255, 0, 0));

        btnLogOut.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnLogOut.setText("Log out");
        btnLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogOutActionPerformed(evt);
            }
        });

        myTab.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        myTab.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                myTabStateChanged(evt);
            }
        });

        SupplierPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SupplierPanelMouseClicked(evt);
            }
        });

        tblSupplier.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier Code", "Supplier Name", "Address"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSupplier.getTableHeader().setReorderingAllowed(false);
        tblSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSupplierMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblSupplierMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblSupplier);

        lblSupplierCode.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblSupplierCode.setText("Supplier Code");

        lblSupplierName.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblSupplierName.setText("Supplier Name");

        lblAddress.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblAddress.setText("Address");

        lblCollaborating.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblCollaborating.setText("Collaborating");

        btnSupplierAddNew.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnSupplierAddNew.setText("Add New");
        btnSupplierAddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierAddNewActionPerformed(evt);
            }
        });

        btnSupplierSave.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnSupplierSave.setText("Save");
        btnSupplierSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierSaveActionPerformed(evt);
            }
        });

        btnSupplierDelete.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnSupplierDelete.setText("Delete");
        btnSupplierDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSupplierDeleteActionPerformed(evt);
            }
        });

        txtSupCode.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        txtAddress.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        txtSupName.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        ckbCollaborating.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblSupCodeWarnning.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        lblSupCodeWarnning.setForeground(new java.awt.Color(255, 0, 0));

        lblSupNameWarnning.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        lblSupNameWarnning.setForeground(new java.awt.Color(255, 0, 0));

        lblAddressWarnning.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        lblAddressWarnning.setForeground(new java.awt.Color(255, 0, 0));

        lblSupplierNotification.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        lblSupplierNotification.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout SupplierPanelLayout = new javax.swing.GroupLayout(SupplierPanel);
        SupplierPanel.setLayout(SupplierPanelLayout);
        SupplierPanelLayout.setHorizontalGroup(
            SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SupplierPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SupplierPanelLayout.createSequentialGroup()
                        .addGroup(SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(SupplierPanelLayout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addGroup(SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(SupplierPanelLayout.createSequentialGroup()
                                        .addComponent(lblSupplierCode)
                                        .addGap(34, 34, 34)
                                        .addComponent(txtSupCode, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(SupplierPanelLayout.createSequentialGroup()
                                        .addComponent(lblSupplierName)
                                        .addGap(28, 28, 28)
                                        .addGroup(SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblSupNameWarnning, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtSupName, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(SupplierPanelLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addGroup(SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(SupplierPanelLayout.createSequentialGroup()
                                        .addComponent(btnSupplierAddNew)
                                        .addGap(36, 36, 36)
                                        .addComponent(btnSupplierSave, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(SupplierPanelLayout.createSequentialGroup()
                                        .addGroup(SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblAddress)
                                            .addComponent(lblCollaborating))
                                        .addGap(59, 59, 59)
                                        .addComponent(ckbCollaborating, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(33, 33, 33)
                                .addComponent(btnSupplierDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SupplierPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAddressWarnning, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSupCodeWarnning, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SupplierPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblSupplierNotification, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        SupplierPanelLayout.setVerticalGroup(
            SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SupplierPanelLayout.createSequentialGroup()
                .addGroup(SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(SupplierPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(SupplierPanelLayout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSupplierCode)
                            .addComponent(txtSupCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSupCodeWarnning, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addGroup(SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSupName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSupplierName, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSupNameWarnning, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAddress)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addComponent(lblAddressWarnning, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCollaborating)
                            .addComponent(ckbCollaborating))
                        .addGap(61, 61, 61)
                        .addGroup(SupplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSupplierSave)
                            .addComponent(btnSupplierDelete)
                            .addComponent(btnSupplierAddNew))))
                .addGap(18, 18, 18)
                .addComponent(lblSupplierNotification, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        myTab.addTab("Supplier", SupplierPanel);

        ItemPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ItemPanelMouseClicked(evt);
            }
        });

        tblItem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Code", "Item Name", "Supplier", "Unit", "Price", "Supplying"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblItem.getTableHeader().setReorderingAllowed(false);
        tblItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblItemMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblItemMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(tblItem);

        btnItemAddNew.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnItemAddNew.setText("Add New");
        btnItemAddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemAddNewActionPerformed(evt);
            }
        });

        btnItemSave.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnItemSave.setText("Save");
        btnItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemSaveActionPerformed(evt);
            }
        });

        btnItemDelete.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnItemDelete.setText("Delete");
        btnItemDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemDeleteActionPerformed(evt);
            }
        });

        lblItemCode.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblItemCode.setText("Item Code");

        lblItemName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblItemName.setText("Item Name");

        cbxItemSupplier.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblItemSupplier.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblItemSupplier.setText("Supplier");

        lblItemUnit.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblItemUnit.setText("Unit");

        lblItemPrice.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblItemPrice.setText("Price");

        lblItemSupplying.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblItemSupplying.setText("Supplying");

        txtItemCode.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtItemName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtItemUnit.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        txtItemPrice.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        lblItemNameWarnning.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        lblItemNameWarnning.setForeground(new java.awt.Color(255, 0, 0));

        lblItemSupplierWarnning.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        lblItemSupplierWarnning.setForeground(new java.awt.Color(255, 0, 0));

        lblItemUnitWarnning.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        lblItemUnitWarnning.setForeground(new java.awt.Color(255, 0, 0));

        lblItemCodeWarnning.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        lblItemCodeWarnning.setForeground(new java.awt.Color(255, 0, 0));

        lblItemPriceWarnning.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        lblItemPriceWarnning.setForeground(new java.awt.Color(255, 0, 0));

        lblItemNotification.setFont(new java.awt.Font("Tahoma", 2, 18)); // NOI18N
        lblItemNotification.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout ItemPanelLayout = new javax.swing.GroupLayout(ItemPanel);
        ItemPanel.setLayout(ItemPanelLayout);
        ItemPanelLayout.setHorizontalGroup(
            ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ItemPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ItemPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblItemName)
                                .addComponent(lblItemCode))
                            .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblItemUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblItemSupplier)
                                .addComponent(lblItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblItemSupplying))
                        .addGap(18, 18, 18)
                        .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblItemUnitWarnning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(ItemPanelLayout.createSequentialGroup()
                                .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblItemNameWarnning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(47, 47, 47))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemPanelLayout.createSequentialGroup()
                                .addComponent(lblItemSupplierWarnning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                            .addGroup(ItemPanelLayout.createSequentialGroup()
                                .addComponent(txtItemUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(ItemPanelLayout.createSequentialGroup()
                                .addComponent(lblItemCodeWarnning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                            .addComponent(lblItemPriceWarnning, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(ItemPanelLayout.createSequentialGroup()
                                .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxItemSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(ItemPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addComponent(btnItemAddNew)
                        .addGap(32, 32, 32)
                        .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemPanelLayout.createSequentialGroup()
                                .addComponent(btnItemDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemPanelLayout.createSequentialGroup()
                                .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnItemSave, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ckbItemSupplying, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(168, 168, 168))))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblItemNotification, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        ItemPanelLayout.setVerticalGroup(
            ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ItemPanelLayout.createSequentialGroup()
                .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ItemPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ItemPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblItemCode)
                            .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblItemCodeWarnning, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblItemName))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblItemNameWarnning, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cbxItemSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblItemSupplier))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblItemSupplierWarnning, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtItemUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblItemUnit))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblItemUnitWarnning, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblItemPrice))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblItemPriceWarnning, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ckbItemSupplying)
                            .addComponent(lblItemSupplying))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addGroup(ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnItemAddNew)
                            .addComponent(btnItemSave)
                            .addComponent(btnItemDelete))
                        .addGap(30, 30, 30)))
                .addComponent(lblItemNotification, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        myTab.addTab("Item", ItemPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(lblWelcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
            .addComponent(myTab)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(lblWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(myTab))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        // TODO add your handling code here:
        if (JOptionPane.showConfirmDialog(this, "Are you sure to LOG OUT ?",
                "Warnning", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
            if (flagSupplierPanel == false) {
                if (checkSupplierUpdate() == false || flagSupplierAddNew == true) {
                    if (JOptionPane.showConfirmDialog(this, "You have not finished your working in Supplier panel\nDo you want to save ?",
                            "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        if (flagSupplierAddNew == true) {
                            if (testInputSupplier() == true) {
                                try {
                                    if (SupplierDAO.checkSupCodeExist(txtSupCode.getText()) == false) {
                                        SupplierDAO.addSupplier(txtSupCode.getText(), txtSupName.getText(), txtAddress.getText(), ckbCollaborating.isSelected());
                                        JOptionPane.showMessageDialog(null, "Add new Supplier successfully");
                                    }
                                } catch (SQLException e) {
                                    JOptionPane.showMessageDialog(null, "Add new supplier error");
                                }
                            }
                        } else {
                            index = tblSupplier.getSelectedRow();
                            if (index >= 0) {
                                if (checkSupplierUpdate() == true) {
                                    JOptionPane.showMessageDialog(null, "You have not edited anything");
                                } else {
                                    if (testInputSupplier() == true) {
                                        try {
                                            SupplierDAO.updateSupplier(txtSupCode.getText(), txtSupName.getText(), txtAddress.getText(), ckbCollaborating.isSelected());
                                            JOptionPane.showMessageDialog(null, "Update successfully");
                                        } catch (SQLException ex) {
                                            JOptionPane.showMessageDialog(null, "Updating supplier error");
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Your input value is invalid");
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (flagItemPanel == false) {
                if (checkItemUpdate() == false || flagItemAddNew == true) {
                    if (JOptionPane.showConfirmDialog(this, "You have not finished your working in Item panel\nDo you want to save ?",
                            "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        if (flagItemAddNew == true) {
                            if (testInputItem() == true) {
                                try {
                                    if (ItemDAO.checkItemCodeExist(txtItemCode.getText()) == false) {
                                        ItemDAO.addItem(txtItemCode.getText(), txtItemName.getText(), txtItemUnit.getText(), Float.parseFloat(txtItemPrice.getText()), ckbItemSupplying.isSelected(), cbxItemSupplier.getSelectedItem().toString());
                                        JOptionPane.showMessageDialog(null, "ADD NEW Item successfully");
                                    }
                                } catch (SQLException e) {
                                    JOptionPane.showMessageDialog(null, "Adding Item error");
                                }
                            }
                        } else {
                            index = tblItem.getSelectedRow();
                            if (index >= 0) {
                                if (checkItemUpdate() == true) {
                                    JOptionPane.showMessageDialog(null, "You have not edited anything");
                                } else {
                                    if (testInputItem() == true) {
                                        try {
                                            ItemDAO.updateItem(txtItemCode.getText(), txtItemName.getText(), txtItemUnit.getText(), Float.parseFloat(txtItemPrice.getText()), ckbItemSupplying.isSelected(), cbxItemSupplier.getSelectedItem().toString());
                                            JOptionPane.showMessageDialog(null, "UPDATE successfully");
                                        } catch (SQLException ex) {
                                            JOptionPane.showMessageDialog(null, "Updating Item error");
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Your input value is invalid");
                                    }
                                    try {
                                        loadItem();
                                    } catch (SQLException ex) {
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.exit(0);
        }


    }//GEN-LAST:event_btnLogOutActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:        
        if (flagSupplierPanel == false) {
            if (checkSupplierUpdate() == false || flagSupplierAddNew == true) {
                if (JOptionPane.showConfirmDialog(this, "You have not finished your working in Supplier panel\nDo you want to save ?",
                        "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (flagSupplierAddNew == true) {
                        if (testInputSupplier() == true) {
                            try {
                                if (SupplierDAO.checkSupCodeExist(txtSupCode.getText()) == false) {
                                    SupplierDAO.addSupplier(txtSupCode.getText(), txtSupName.getText(), txtAddress.getText(), ckbCollaborating.isSelected());
                                    JOptionPane.showMessageDialog(null, "Add new Supplier successfully");
                                }
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, "Add new supplier error");
                            }
                        }
                    } else {
                        index = tblSupplier.getSelectedRow();
                        if (index >= 0) {
                            if (checkSupplierUpdate() == true) {
                                JOptionPane.showMessageDialog(null, "You have not edited anything");
                            } else {
                                if (testInputSupplier() == true) {
                                    try {
                                        SupplierDAO.updateSupplier(txtSupCode.getText(), txtSupName.getText(), txtAddress.getText(), ckbCollaborating.isSelected());
                                        JOptionPane.showMessageDialog(null, "Update successfully");
                                    } catch (SQLException ex) {
                                        JOptionPane.showMessageDialog(null, "Updating supplier error");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Your input value is invalid");
                                }
                            }
                        }
                    }
                }
            }
        } else if (flagItemPanel == false) {
            if (checkItemUpdate() == false || flagItemAddNew == true) {
                if (JOptionPane.showConfirmDialog(this, "You have not finished your working in Item panel\nDo you want to save ?",
                        "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (flagItemAddNew == true) {
                        if (testInputItem() == true) {
                            try {
                                if (ItemDAO.checkItemCodeExist(txtItemCode.getText()) == false) {
                                    ItemDAO.addItem(txtItemCode.getText(), txtItemName.getText(), txtItemUnit.getText(), Float.parseFloat(txtItemPrice.getText()), ckbItemSupplying.isSelected(), cbxItemSupplier.getSelectedItem().toString());
                                    JOptionPane.showMessageDialog(null, "Add new Item successfully");
                                }
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, "Add new Item error");
                            }
                        }
                    } else {
                        index = tblItem.getSelectedRow();
                        if (index >= 0) {
                            if (checkItemUpdate() == true) {
                                JOptionPane.showMessageDialog(null, "You have not edited anything");
                            } else {
                                if (testInputItem() == true) {
                                    try {
                                        ItemDAO.updateItem(txtItemCode.getText(), txtItemName.getText(), txtItemUnit.getText(), Float.parseFloat(txtItemPrice.getText()), ckbItemSupplying.isSelected(), cbxItemSupplier.getSelectedItem().toString());
                                        JOptionPane.showMessageDialog(null, "Update successfully");
                                    } catch (SQLException ex) {
                                        JOptionPane.showMessageDialog(null, "Updating Item error");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Your input value is invalid");
                                }
                                try {
                                    loadItem();
                                } catch (SQLException ex) {
                                }
                            }
                        }
                    }
                }
            }
        }
        System.exit(0);

    }//GEN-LAST:event_formWindowClosing

    private void myTabStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_myTabStateChanged
        // TODO add your handling code here:       
        try {
            loadSupplier();
            loadItem();
        } catch (SQLException ex) {
        }

        if (flagSupplierPanel == false) {                               //đã bấm chuyển, từ Supplier qua Item         
            if (checkSupplierUpdate() == false || flagItemAddNew == true || btnSupplierAddNew.getText().equals("Cancel")) {
                if (JOptionPane.showConfirmDialog(this, "You have not finished your working in Supplier panel\nDo you want to save ?",
                        null, JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                    if (btnSupplierAddNew.getText().equals("Cancel")) {            // bên kia đã bấm ADD NEW
                        if (txtSupCode.getText().isEmpty() && txtSupName.getText().isEmpty() && txtAddress.getText().isEmpty() && !ckbCollaborating.isSelected()) {
                            JOptionPane.showMessageDialog(null, "You have input nothing");
                        }
                        if (checkInputSupplier() == true) {
                            try {
                                if (SupplierDAO.checkSupCodeExist(txtSupCode.getText()) == false) {
                                    SupplierDAO.addSupplier(txtSupCode.getText(), txtSupName.getText(), txtAddress.getText(), ckbCollaborating.isSelected());
                                    JOptionPane.showMessageDialog(null, "ADD NEW Supplier successfully");
                                    loadSupplier();
                                    loadItem();
                                }
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, "Adding Supplier error");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Your input value is invalid");
                        }
                    } else if (checkSupplierUpdate() == false) {
                        String nameCheck = txtSupName.getText();
                        String addressCheck = txtAddress.getText();
                        boolean collaboratingCheck = ckbCollaborating.isSelected();
                        if (!nameCheck.equals(listSupplier.get(index).getSupName())
                                || !addressCheck.equals(listSupplier.get(index).getAddress())
                                || (collaboratingCheck != listSupplier.get(index).isCollaborating())) {
                            try {
                                SupplierDAO.updateSupplier(txtSupCode.getText(), txtSupName.getText(), txtAddress.getText(), ckbCollaborating.isSelected());
                                JOptionPane.showMessageDialog(null, "UPDATE successfully");
                                loadSupplier();
                                loadItem();
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, "Updating supplier error");
                            }
                        }
                    }
                }
                btnSupplierAddNew.setText("Add New");
                flagSupplierAddNew = false;
                flagSupplierSave = true;
                loadSupplier();
            }

            flagSupplierPanel = true;
            flagItemPanel = false;
            resetSupplierPanel();
        } else if (flagItemPanel == false) {                      // đã bấm chuyển tử Item qua Supplier  
            if (checkItemUpdate() == false || flagItemAddNew == true || btnItemAddNew.getText().equals("Cancel")) {
                //JOptionPane.showMessageDialog(null, checkItemUpdate() + " and " + flagItemAddNew + " and " + btnItemAddNew.getText().equals("Cancel"));
                if (JOptionPane.showConfirmDialog(this, "You have not finished your working in Item panel\nDo you want to save ?",
                        "Warnning", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                    if (btnItemAddNew.getText().equals("Cancel")) {
                        if (txtItemCode.getText().isEmpty() && txtItemName.getText().isEmpty() && cbxItemSupplier.getSelectedIndex() == 0 && txtItemUnit.getText().isEmpty() && txtItemPrice.getText().isEmpty() && !ckbItemSupplying.isSelected()) {
                            JOptionPane.showMessageDialog(null, "You have input nothing");
                        }
                        if (checkInputItem() == true) {
                            try {
                                if (!ItemDAO.checkItemCodeExist(txtItemCode.getText())) {
                                    ItemDAO.addItem(txtItemCode.getText(), txtItemName.getText(), txtItemUnit.getText(), Float.parseFloat(txtItemPrice.getText()), ckbItemSupplying.isSelected(), cbxItemSupplier.getSelectedItem().toString().split(" - ")[0]);
                                    JOptionPane.showMessageDialog(null, "ADD NEW Item successfully");
                                    loadSupplier();
                                    loadItem();
                                }
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, "Adding Item error");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Your input value is invalid");
                        }
                    } else if (checkItemUpdate() == false) {
                        String nameCheck = txtItemName.getText();
                        String supplierCheck = cbxItemSupplier.getSelectedItem().toString();
                        String unitCheck = txtItemUnit.getText();
                        float priceCheck = Float.parseFloat(txtItemPrice.getText());
                        boolean supplyingCheck = ckbItemSupplying.isSelected();
                        if (!nameCheck.equals(listItem.get(index).getItemName())
                                // || !supplierCheck.equals(defaultComboBoxModel.getSelectedItem())
                                || !supplierCheck.equals(listItem.get(index).getItemSupCode() + " - " + listItem.get(index).getItemSupName())
                                || !unitCheck.equals(listItem.get(index).getUnit())
                                || priceCheck != Float.parseFloat(txtItemPrice.getText())
                                || (supplyingCheck != listItem.get(index).isSupplying())) {
                            try {
                                ItemDAO.updateItem(txtItemCode.getText(), txtItemName.getText(), txtItemUnit.getText(), Float.parseFloat(txtItemPrice.getText()), ckbItemSupplying.isSelected(), cbxItemSupplier.getSelectedItem().toString());
                                JOptionPane.showMessageDialog(null, "UPDATE Item successfully");
                                loadSupplier();
                                loadItem();
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(null, "Updating item error");
                            }
                        }
                    }
                }
                btnItemAddNew.setText("Add New");
                flagItemAddNew = false;
                flagItemSave = true;
            }
            flagSupplierPanel = false;
            flagItemPanel = true;
            resetItemPanel();
        }
    }//GEN-LAST:event_myTabStateChanged

    private void btnSupplierAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierAddNewActionPerformed
        // TODO add your handling code here:   

        index = tblSupplier.getSelectedRow();
        if (index >= 0 && checkSupplierUpdate() == false) {                  // đã chọn 1 thằng nào đó        UPDATE             
            JOptionPane.showMessageDialog(null, "You are editing a certain Supplier\n Please complete this first");
        } else {                                           // chưa chọn thằng nào hết
            if (flagSupplierAddNew == false) {             //   Nhấn lần 1: ADD NEW
                clearText();
                clearNotification();
                tblSupplier.clearSelection();
                tblSupplier.setEnabled(false);
                txtSupCode.setEditable(true);
                txtSupName.setEditable(true);
                txtAddress.setEditable(true);
                ckbCollaborating.setEnabled(true);
                btnSupplierAddNew.setText("Cancel");
                btnSupplierSave.setEnabled(true);
                btnSupplierDelete.setEnabled(false);
                flagSupplierAddNew = true;
            } else {                                       // Nhấn lần 2: CANCEL
                int choose = JOptionPane.showConfirmDialog(this, "Do you want to save this Supplier ?", "Warnning", JOptionPane.YES_NO_CANCEL_OPTION);
                switch (choose) {
                    case JOptionPane.YES_OPTION: {
                        if (txtSupCode.getText().isEmpty() && txtSupName.getText().isEmpty() && txtAddress.getText().isEmpty() && !ckbCollaborating.isSelected()) {
                            JOptionPane.showMessageDialog(null, "You have input nothing");
                        }
                        if (testInputSupplier() == true) {
                            try {
                                SupplierDAO.addSupplier(txtSupCode.getText(), txtSupName.getText(), txtAddress.getText(), ckbCollaborating.isSelected());
                                JOptionPane.showMessageDialog(null, "SAVE Supplier successfully");
                            } catch (SQLException e) {
                                JOptionPane.showMessageDialog(null, "Your input value is invalid");
                            }
                        }
                        btnSupplierAddNew.setText("Add New");
                        flagSupplierAddNew = false;
                        flagSupplierSave = true;
                        resetSupplierPanel();
                        loadSupplier();
                        break;
                    }
                    case JOptionPane.NO_OPTION: {
                        btnSupplierAddNew.setText("Add New");
                        flagSupplierAddNew = false;
                        resetSupplierPanel();
                        break;
                    }
                    case JOptionPane.CANCEL_OPTION:
                        break;
                    default:
                        break;
                }
            }
        }

    }//GEN-LAST:event_btnSupplierAddNewActionPerformed

    private void btnSupplierSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierSaveActionPerformed
        // TODO add your handling code here:
        clearNotification();
        if (flagSupplierAddNew == true) {                       //save của add new
            if (txtSupCode.getText().isEmpty() && txtSupName.getText().isEmpty() && txtAddress.getText().isEmpty() && !ckbCollaborating.isSelected()) {
                JOptionPane.showMessageDialog(null, "You have input nothing");
            }
            if (testInputSupplier() == true) {
                try {
                    if (SupplierDAO.checkSupCodeExist(txtSupCode.getText()) == false) {
                        SupplierDAO.addSupplier(txtSupCode.getText(), txtSupName.getText(), txtAddress.getText(), ckbCollaborating.isSelected());
                        JOptionPane.showMessageDialog(null, "ADD NEW Supplier successfully");
                        btnSupplierAddNew.setText("Add New");
                        loadSupplier();
                        resetSupplierPanel();
                        flagSupplierAddNew = false;
                        flagSupplierSave = true;
                    } else {
                        lblSupCodeWarnning.setText("Your input code is already exist");
                        txtSupCode.setText("");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Adding supplier error");
                }
            }
        } else {                             //save của update         
            index = tblSupplier.getSelectedRow();
            if (index >= 0) {
                if (checkSupplierUpdate() == true) {
                    JOptionPane.showMessageDialog(null, "You have not edited anything");
                } else {
                    if (testInputSupplier() == true) {
                        try {
                            SupplierDAO.updateSupplier(txtSupCode.getText(), txtSupName.getText(), txtAddress.getText(), ckbCollaborating.isSelected());
                            JOptionPane.showMessageDialog(null, "UPDATE Supplier successfully");
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Updating Supplier error");
                        }
                    } else {
                        clearNotification();
                        JOptionPane.showMessageDialog(null, "Your input value is invalid");
                    }

                }
                loadSupplier();
                resetSupplierPanel();
            }
        }
    }//GEN-LAST:event_btnSupplierSaveActionPerformed

    private void btnSupplierDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSupplierDeleteActionPerformed
        // TODO add your handling code here:
        clearNotification();
        index = tblSupplier.getSelectedRow();
        if (index < 0) {
            lblSupplierNotification.setText("Please choose Supplier you want to DELETE  ");
        } else {
            try {
                if (SupplierDAO.checkSupplying(listSupplier.get(index).getSupCode())) {
                    lblSupplierNotification.setText("Cannot delete the supplier is using  ");
                } else {
                    if (JOptionPane.showConfirmDialog(this, "Are you sure you want to DELETE this supplier ?",
                            "Warnning", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                        try {
                            SupplierDAO.deleteSupplier(listSupplier.get(index).getSupCode());
                            JOptionPane.showMessageDialog(null, "DELETE Supplier successfully");
                            resetSupplierPanel();
                            loadSupplier();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Deleting Supplier error");
                        }
                    }
                }
            } catch (SQLException ex) {
            }

        }

    }//GEN-LAST:event_btnSupplierDeleteActionPerformed

    private void tblSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSupplierMouseClicked
        // TODO add your handling code here:
        clearNotification();
        index = tblSupplier.getSelectedRow();
        if (index >= 0) {
            txtSupCode.setText(listSupplier.get(index).getSupCode());
            txtSupName.setText(listSupplier.get(index).getSupName());
            txtAddress.setText(listSupplier.get(index).getAddress());
            if (listSupplier.get(index).isCollaborating()) {
                ckbCollaborating.setSelected(true);
            } else {
                ckbCollaborating.setSelected(false);
            }
            txtSupName.setEditable(true);
            txtAddress.setEditable(true);
            ckbCollaborating.setEnabled(true);
            btnSupplierSave.setEnabled(true);
            btnSupplierDelete.setEnabled(true);
        }
    }//GEN-LAST:event_tblSupplierMouseClicked

    private void btnItemAddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemAddNewActionPerformed
        // TODO add your handling code here:
        index = tblItem.getSelectedRow();
        if (index >= 0 && checkItemUpdate() == false) {
            JOptionPane.showMessageDialog(null, "You are Editing a certain Item\nPlease complete this first");
        } else {
            if (flagItemAddNew == false) {
                clearText();
                clearNotification();
                tblItem.clearSelection();
                tblItem.setEnabled(false);
                txtItemCode.setEditable(true);
                txtItemName.setEditable(true);
                cbxItemSupplier.setEnabled(true);
                txtItemUnit.setEditable(true);
                txtItemPrice.setEditable(true);
                ckbItemSupplying.setEnabled(true);
                btnItemAddNew.setText("Cancel");
                btnItemSave.setEnabled(true);
                btnItemDelete.setEnabled(false);
                flagItemAddNew = true;
            } else {
                int choose = JOptionPane.showConfirmDialog(null, "Do you want to save this Item ?", "Warnning", JOptionPane.YES_NO_CANCEL_OPTION);
                switch (choose) {
                    case JOptionPane.YES_OPTION: {
                        if (txtItemCode.getText().isEmpty() && txtItemName.getText().isEmpty() && cbxItemSupplier.getSelectedIndex() == 0 && txtItemUnit.getText().isEmpty() && txtItemPrice.getText().isEmpty() && !ckbItemSupplying.isSelected()) {
                            JOptionPane.showMessageDialog(null, "You have input nothing");
                        }
                        if (testInputItem() == true) {
                            try {
                                ItemDAO.addItem(txtItemCode.getText(), txtItemName.getText(), txtItemUnit.getText(), Float.parseFloat(txtItemPrice.getText()), ckbItemSupplying.isSelected(), cbxItemSupplier.getSelectedItem().toString());
                                JOptionPane.showMessageDialog(null, "SAVE Item successfully");
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Your input value is invalid");
                            }
                        }
                        btnItemAddNew.setText("Add New");
                        flagItemAddNew = false;
                        flagItemSave = true;
                        resetItemPanel();
                        try {
                            loadItem();
                        } catch (SQLException ex) {
                        }
                        break;
                    }
                    case JOptionPane.NO_OPTION:
                        btnItemAddNew.setText("Add New");
                        flagItemAddNew = false;
                        resetItemPanel();
                        try {
                            loadItem();
                        } catch (SQLException ex) {
                        }
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        break;
                    default:
                        break;
                }
            }
        }
    }//GEN-LAST:event_btnItemAddNewActionPerformed

    private void btnItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemSaveActionPerformed
        // TODO add your handling code here:
        clearNotification();
        if (flagItemAddNew == true) {                       //save của add new
            if (txtItemCode.getText().isEmpty() && txtItemName.getText().isEmpty() && cbxItemSupplier.getSelectedIndex() == 0 && txtItemUnit.getText().isEmpty() && txtItemPrice.getText().isEmpty() && !ckbItemSupplying.isSelected()) {
                JOptionPane.showMessageDialog(null, "You have input nothing");
            }
            if (testInputItem() == true) {
                try {
                    if (ItemDAO.checkItemCodeExist(txtItemCode.getText()) == false) {
                        ItemDAO.addItem(txtItemCode.getText(), txtItemName.getText(), txtItemUnit.getText(), Float.parseFloat(txtItemPrice.getText()), ckbItemSupplying.isSelected(), cbxItemSupplier.getSelectedItem().toString());
                        JOptionPane.showMessageDialog(null, "ADD NEW Item successfully");
                        btnItemAddNew.setText("Add New");
                        loadItem();
                        resetItemPanel();
                        flagItemSave = true;
                    } else {
                        lblItemCodeWarnning.setText("Your input code is already exist");
                        txtItemCode.setText("");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Adding Item error");
                }
            }
        } else {                                                //save của update
            index = tblItem.getSelectedRow();
            if (index >= 0) {
                if (checkItemUpdate() == true) {
                    JOptionPane.showMessageDialog(null, "You have not edited anything");
                } else {
                    if (testInputItem() == true) {
                        try {
                            ItemDAO.updateItem(txtItemCode.getText(), txtItemName.getText(), txtItemUnit.getText(), Float.parseFloat(txtItemPrice.getText()), ckbItemSupplying.isSelected(), cbxItemSupplier.getSelectedItem().toString().split(" - ")[0]);
                            JOptionPane.showMessageDialog(null, "UPDATE Item successfully");
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Updating Item error");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Your input value is invalid");
                        clearNotification();
                    }
                }
                try {
                    loadItem();
                } catch (SQLException ex) {
                }
                resetItemPanel();
            }
        }
    }//GEN-LAST:event_btnItemSaveActionPerformed

    private void btnItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemDeleteActionPerformed
        // TODO add your handling code here:
        clearNotification();
        index = tblItem.getSelectedRow();
        if (index < 0) {
            lblSupplierNotification.setText("Please choose Item you want to DELETE  ");
        } else {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to DELETE this Item ?",
                    "Warnning", JOptionPane.YES_NO_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    ItemDAO.deleteItem(listItem.get(index).getItemCode());
                    JOptionPane.showMessageDialog(null, "DELETE Item successfully");
                    resetSupplierPanel();
                    loadSupplier();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Deleting Item error");
                }
            }
        }

    }//GEN-LAST:event_btnItemDeleteActionPerformed

    private void tblItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemMouseClicked
        // TODO add your handling code here:
        clearNotification();
        index = tblItem.getSelectedRow();
        if (index >= 0) {
            txtItemCode.setText(listItem.get(index).getItemCode());
            txtItemName.setText(listItem.get(index).getItemName());
            txtItemUnit.setText(listItem.get(index).getUnit());
            txtItemPrice.setText("" + listItem.get(index).getPrice());
            if (listItem.get(index).isSupplying()) {
                ckbItemSupplying.setSelected(true);
            } else {
                ckbItemSupplying.setSelected(false);
            }
            defaultComboBoxModel.setSelectedItem(listItem.get(index).getItemSupCode() + " - " + listItem.get(index).getItemSupName());
            cbxItemSupplier.setSelectedItem(listItem.get(index).getItemSupCode() + " - " + listItem.get(index).getItemSupName());

            txtItemName.setEditable(true);
            cbxItemSupplier.setEnabled(true);
            txtItemUnit.setEditable(true);
            txtItemPrice.setEditable(true);
            ckbItemSupplying.setEnabled(true);
            btnItemSave.setEnabled(true);
            btnItemDelete.setEnabled(true);
        }
    }//GEN-LAST:event_tblItemMouseClicked

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        tblSupplier.clearSelection();
        tblItem.clearSelection();
    }//GEN-LAST:event_formMouseClicked

    private void SupplierPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SupplierPanelMouseClicked
        // TODO add your handling code here:
//        tblSupplier.clearSelection();
//        tblItem.clearSelection();
        flagSupplierPanel = true;
        flagItemPanel = false;
    }//GEN-LAST:event_SupplierPanelMouseClicked

    private void ItemPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ItemPanelMouseClicked
        // TODO add your handling code here:
//        tblSupplier.clearSelection();
//        tblItem.clearSelection();
        flagSupplierPanel = false;
        flagItemPanel = true;
    }//GEN-LAST:event_ItemPanelMouseClicked

    private void tblSupplierMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSupplierMouseReleased
        // TODO add your handling code here:
        int iindexx = tblSupplier.getSelectedRow();
        if (iindexx >= 0) {
            tblSupplier.setRowSelectionInterval(iindexx, iindexx);
            tblSupplierMouseClicked(null);
        }
    }//GEN-LAST:event_tblSupplierMouseReleased

    private void tblItemMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblItemMouseReleased
        // TODO add your handling code here:
        int iindexx = tblItem.getSelectedRow();
        if (iindexx >= 0) {
            tblItem.setRowSelectionInterval(iindexx, iindexx);
            tblItemMouseClicked(null);
        }
    }//GEN-LAST:event_tblItemMouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainFrame dialog = new MainFrame(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ItemPanel;
    private javax.swing.JPanel SupplierPanel;
    private javax.swing.JButton btnItemAddNew;
    private javax.swing.JButton btnItemDelete;
    private javax.swing.JButton btnItemSave;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnSupplierAddNew;
    private javax.swing.JButton btnSupplierDelete;
    private javax.swing.JButton btnSupplierSave;
    private javax.swing.JComboBox<String> cbxItemSupplier;
    private javax.swing.JCheckBox ckbCollaborating;
    private javax.swing.JCheckBox ckbItemSupplying;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblAddressWarnning;
    private javax.swing.JLabel lblCollaborating;
    private javax.swing.JLabel lblItemCode;
    private javax.swing.JLabel lblItemCodeWarnning;
    private javax.swing.JLabel lblItemName;
    private javax.swing.JLabel lblItemNameWarnning;
    private javax.swing.JLabel lblItemNotification;
    private javax.swing.JLabel lblItemPrice;
    private javax.swing.JLabel lblItemPriceWarnning;
    private javax.swing.JLabel lblItemSupplier;
    private javax.swing.JLabel lblItemSupplierWarnning;
    private javax.swing.JLabel lblItemSupplying;
    private javax.swing.JLabel lblItemUnit;
    private javax.swing.JLabel lblItemUnitWarnning;
    private javax.swing.JLabel lblSupCodeWarnning;
    private javax.swing.JLabel lblSupNameWarnning;
    private javax.swing.JLabel lblSupplierCode;
    private javax.swing.JLabel lblSupplierName;
    private javax.swing.JLabel lblSupplierNotification;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JTabbedPane myTab;
    private javax.swing.JTable tblItem;
    private javax.swing.JTable tblSupplier;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtItemPrice;
    private javax.swing.JTextField txtItemUnit;
    private javax.swing.JTextField txtSupCode;
    private javax.swing.JTextField txtSupName;
    // End of variables declaration//GEN-END:variables
}
