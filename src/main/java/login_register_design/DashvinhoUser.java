/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login_register_design;

/**
 *
 * @author HomePC
 */
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class DashvinhoUser extends javax.swing.JFrame {

    public String status_edit = "new";
    public ArrayList<ComboItem> cClass;
    public ArrayList<ComboItem> cCountry;
    public ArrayList<ComboItem> cMarca;
    public ArrayList<ComboItem> cTipoU;

    //metodo para inserção de novos registos no banco
    private boolean insert(String vinho, int size, float alcohol, float price, int harvest, int brand, int country, int classe, int tpgrape, Blob image) {
        PreparedStatement st;
        int ur;

        //verifica se o campo de texto é vazio
        if (!(vinho != null && !vinho.isEmpty())) {
            showMessageDialog(null, "Vinho não pode ser vazia!");
            return false;
        }

        //faz a inserção no banco de dados
        try {
            String query = "INSERT INTO vinho (Name, Size, Alcohol, Price, Harvest, fk_Brand, fk_Country, fk_Class, fk_TypeGrape, Image)\n"
                    + "    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            st = My_CNX.getConnection().prepareStatement(query);
            st.setString(1, vinho);
            st.setInt(2, size);
            st.setFloat(3, alcohol);
            st.setFloat(4, price);
            st.setInt(5, harvest);
            st.setInt(6, brand);
            st.setInt(7, country);
            st.setInt(8, classe);
            st.setInt(9, tpgrape);
            st.setBlob(10, image);
            ur = st.executeUpdate();
            //retorna se a inserção inseriu linhas, ou não (caso erro ur valerá 0 ou -1 e retornará falso)
            return (ur > 0);
        } catch (SQLException ex) {
            Logger.getLogger(DashvinhoUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        //retorna falso caso try catch deu error
        return false;
    }

    //metodo para deletar registros por id
    private boolean delete(int id) {
        PreparedStatement st;
        int ur;

        //faz a deleção no banco
        try {
            String query = "DELETE FROM vinho WHERE ID = ?";
            st = My_CNX.getConnection().prepareStatement(query);
            st.setInt(1, id);
            ur = st.executeUpdate();
            //retorna se a deleção deletou linhas, ou não (caso erro ur valerá 0 ou -1 e retornará falso)
            return (ur > 0);
        } catch (SQLException ex) {
            Logger.getLogger(DashvinhoUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        //retorna falso caso try catch deu errorF
        return false;
    }

    //metodo para fazer o update de marca por ID
    private boolean update(int id, String vinho, int size, float alcohol, float price, int harvest, int brand, int country, int classe, int tpgrape, Blob image) {
        PreparedStatement st;
        int ur;

        //verifica se o campo de texto é vazio
        if (!(vinho != null && !vinho.isEmpty())) {
            showMessageDialog(null, "Vinho não pode ser vazia!");
            return false;
        }

        //faz a inserção no banco de dados
        try {
            String query = "UPDATE vinho SET Name = ?, Size = ?, Alcohol = ?, Price = ?, Harvest = ?, fk_Brand = ?, "
                    + "fk_Country = ?, fk_Class = ?, fk_TypeGrape = ?, Image = ? WHERE id = ?";
            
            st = My_CNX.getConnection().prepareStatement(query);
            st.setString(1, vinho);
            st.setInt(2, size);
            st.setFloat(3, alcohol);
            st.setFloat(4, price);
            st.setInt(5, harvest);
            st.setInt(6, brand);
            st.setInt(7, country);
            st.setInt(8, classe);
            st.setInt(9, tpgrape);
            st.setBlob(10, image);
            st.setInt(11, id);
            ur = st.executeUpdate();
            
            //retorna se a inserção inseriu linhas, ou não (caso erro ur valerá 0 ou -1 e retornará falso)
            return (ur > 0);
        } catch (SQLException ex) {
            Logger.getLogger(DashvinhoUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        //retorna falso caso try catch deu error
        return false;

    }

    // faz a atualização dos registros na tabela, ou seja
    // faz um select no banco de dados e coloca o retorno dele na tabela
    // possui um parametro string que é utilizado no where em formato de "like"
    private ArrayList<ComboItem> get_dropdown(String table, String column) {

        PreparedStatement st;
        ResultSet rs;
        String r_marca;
        String r_id;
        ArrayList<ComboItem> items = new ArrayList<>();
        try {
            String query = "SELECT id, " + column + " FROM " + table;

            st = My_CNX.getConnection().prepareStatement(query);
            rs = st.executeQuery();

            // para cada linha retornada do banco de dados, ele adicionara uma linha na tabela 
            while (rs.next()) {
                r_marca = rs.getString(column);
                r_id = rs.getInt("id") + "";
                items.add(new ComboItem(r_marca, r_id));
            }
            return (items);

        } catch (SQLException ex) {
            Logger.getLogger(DashvinhoUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int findCI(String key, ArrayList<ComboItem> is) {

        Iterator<ComboItem> iterator = is.iterator();
        int it = 0;
        while (iterator.hasNext()) {
            ComboItem i = iterator.next();
            if (i.getValue().equals(key)) {
                return it;
            }
            it++;
        }
        return -1;
    }

    private void load_table(String where) {
        PreparedStatement st;
        ResultSet rs;
        String r_Name, r_brand, r_country, r_class, r_typegrape;
        int r_Size, r_Alcohol, r_Harvest, r_id;
        Float r_Price;

        try {
            //String query = "SELECT id, brand FROM vinho WHERE brand like ?";
            String query = ""
                    + "select "
                    + "	v.id, "
                    + "	v.Name, "
                    + "	v.Size, "
                    + "	v.Alcohol, "
                    + "	v.Price, "
                    + "	v.Harvest, "
                    + "	b.brand, "
                    + "	ct.country, "
                    + "	cl.class, "
                    + "	t.typegrape "
                    + "from "
                    + "	vinho v "
                    + "inner join "
                    + "	vinho_brand b "
                    + "on "
                    + "	b.id = v.fk_Brand "
                    + "inner join "
                    + "	vinho_country ct "
                    + "on "
                    + "	ct.id = v.fk_Country "
                    + "inner join "
                    + "	vinho_class cl "
                    + "on "
                    + "	cl.id = v.fk_Class "
                    + "inner join "
                    + "	vinho_TypeGrape t "
                    + "on "
                    + "	t.id = v.fk_TypeGrape "
                    + "where v.Name like ?";

            st = My_CNX.getConnection().prepareStatement(query);

            //Linha abaixo verifica se where é nulo ou vazio, caso for, o where recebera "%" para trazer todos os registros 
            //caso não for, where recebera "%"+valor de where+"%" para funcionar na clausula "like" do sql
            where = (where != null && !where.isEmpty()) ? "%" + where + "%" : "%";

            st.setString(1, where);
            rs = st.executeQuery();
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

            // limpa a tabela para que ela possa receber o novo resultado
            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                model.removeRow(i);
            }

            // para cada linha retornada do banco de dados, ele adicionara uma linha na tabela 
            while (rs.next()) {
                r_id = rs.getInt("id");
                r_Name = rs.getString("Name");
                r_Size = rs.getInt("Size");
                r_Alcohol = rs.getInt("Alcohol");
                r_Price = rs.getFloat("Price");
                r_Harvest = rs.getInt("Harvest");
                r_brand = rs.getString("brand");
                r_country = rs.getString("country");
                r_class = rs.getString("class");
                r_typegrape = rs.getString("typegrape");

                model.addRow(new Object[]{r_id, r_Name, r_Size, r_Alcohol, r_Price, r_Harvest, r_brand, r_country, r_class, r_typegrape});
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashvinhoUser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void select_vinho(String id) {

        PreparedStatement st;
        ResultSet rs;
        String r_Name = "", r_brand = "";
        int r_Size = 0, r_Alcohol = 0, r_Harvest = 0, r_fkbrand = 0, r_fkcountry = 0, r_fkclass = 0, r_fktypegrape = 0, r_id = 0;
        Float r_Price = new Float(0);

        try {
            //String query = "SELECT id, brand FROM vinho WHERE brand like ?";
            String query = "SELECT id, Name, Size, Alcohol, Price, Image, Harvest, fk_Brand, fk_Class, fk_Country, fk_TypeGrape FROM vinho WHERE id = ?";

            st = My_CNX.getConnection().prepareStatement(query);
            st.setString(1, id);
            rs = st.executeQuery();

            // para cada linha retornada do banco de dados, ele adicionara uma linha na tabela 
            while (rs.next()) {
                r_id = rs.getInt("id");
                r_Name = rs.getString("Name");
                r_Size = rs.getInt("Size");
                r_Alcohol = rs.getInt("Alcohol");
                r_Price = rs.getFloat("Price");
                r_Harvest = rs.getInt("Harvest");
                r_fkbrand = rs.getInt("fk_Brand");
                r_fkcountry = rs.getInt("fk_Class");
                r_fkclass = rs.getInt("fk_Country");
                r_fktypegrape = rs.getInt("fk_TypeGrape");
            }
            
            jLabel_id.setText(r_id + "");
            txt_alcool.setText(r_Alcohol + "");
            txt_nome.setText(r_Name);
            txt_preco.setText(r_Price + "");
            txt_safra.setText(r_Harvest + "");
            txt_tamanho.setText(r_Size + "");
            jSelection_marca.setSelectedIndex(findCI(r_fkbrand + "", cMarca));
            jSelection_Country.setSelectedIndex(findCI(r_fkcountry + "", cCountry));
            jSelection_class.setSelectedIndex(findCI(r_fkclass + "", cClass));
            jSelection_typegrape.setSelectedIndex(findCI(r_fktypegrape + "", cTipoU));

        } catch (SQLException ex) {
            Logger.getLogger(DashvinhoUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //initCustom metodoo que é executado após o initCoomponents
    // está aqui para adicionar um listener na tabela, para que sempre que uma
    // seleção ocorra em alguma linha, ele jogue os dados para o formulario de edição
    private void initCustom() {
        jTable1.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            if (jTable1.getSelectedRow() > -1) {

                //quando a linha é selecionada, ele seta os valores para o form (label ID e text field brand)
                String vinho_selecionade;
                vinho_selecionade = jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString();

                select_vinho(vinho_selecionade);

            }
        });

        cMarca = get_dropdown("vinho_brand", "brand");
        cCountry = get_dropdown("vinho_country", "country");
        cClass = get_dropdown("vinho_class", "class");
        cTipoU = get_dropdown("vinho_typegrape", "typegrape");

        DefaultComboBoxModel newModel = new DefaultComboBoxModel(cMarca.toArray());
        jSelection_marca.setModel(newModel);

        newModel = new DefaultComboBoxModel(cClass.toArray());
        jSelection_class.setModel(newModel);

        newModel = new DefaultComboBoxModel(cCountry.toArray());
        jSelection_Country.setModel(newModel);

        newModel = new DefaultComboBoxModel(cTipoU.toArray());
        jSelection_typegrape.setModel(newModel);

    }

    public DashvinhoUser() {
        initComponents();
        initCustom();
        load_table(null);
        this.setLocationRelativeTo (null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSelection_Country1 = new javax.swing.JComboBox<>();
        addTipoUva = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        JTextField_vinhosearch = new javax.swing.JTextField();
        JButton_vinhosearchbutton = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel_id = new javax.swing.JLabel();
        txt_nome = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txt_safra = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txt_preco = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txt_alcool = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txt_tamanho = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jSelection_typegrape = new javax.swing.JComboBox<>();
        jSelection_Country = new javax.swing.JComboBox<>();
        jSelection_class = new javax.swing.JComboBox<>();
        jSelection_marca = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();

        jSelection_Country1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        addTipoUva.setText("+");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        JButton_vinhosearchbutton.setText("Buscar");
        JButton_vinhosearchbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButton_vinhosearchbuttonActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Nome:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Id:");

        jLabel_id.setText("0");

        txt_nome.setEnabled(false);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nome", "Tamanho", "Teor", "Preço", "Safra", "Marca", "País", "Classificação", "Tipo de Uva"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Float.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
        }

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Marca:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Classificação:");

        txt_safra.setEnabled(false);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Safra:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("País:");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("Tipo de Uva:");

        txt_preco.setEnabled(false);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Preço(R$):");

        txt_alcool.setEnabled(false);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("Teor(%):");

        txt_tamanho.setEnabled(false);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("Tamanho(mL):");

        jSelection_typegrape.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jSelection_Country.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jSelection_class.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jSelection_marca.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jSelection_marca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSelection_marcaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(JTextField_vinhosearch, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButton_vinhosearchbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(txt_nome)
                        .addGap(35, 35, 35))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel_id))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txt_preco))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_alcool, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_tamanho, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12)
                            .addComponent(jLabel13)
                            .addComponent(jLabel7)
                            .addComponent(jSelection_typegrape, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSelection_Country, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSelection_class, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSelection_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_safra, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JTextField_vinhosearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JButton_vinhosearchbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel_id))
                            .addComponent(jLabel17)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel16)
                                .addComponent(jLabel15))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_alcool, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_preco, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_tamanho, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_nome, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSelection_marca, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSelection_class, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_safra, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSelection_Country, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSelection_typegrape, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Dashboard - Vinho");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 928, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 598, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JButton_vinhosearchbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButton_vinhosearchbuttonActionPerformed
        // carrega a tabela com o parametro de pesquisa presente no text field de pesquisa
        load_table(JTextField_vinhosearch.getText());
    }//GEN-LAST:event_JButton_vinhosearchbuttonActionPerformed

    private void jSelection_marcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSelection_marcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jSelection_marcaActionPerformed

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
            java.util.logging.Logger.getLogger(DashvinhoUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashvinhoUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashvinhoUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashvinhoUser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashvinhoUser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButton_vinhosearchbutton;
    private javax.swing.JTextField JTextField_vinhosearch;
    private javax.swing.JButton addTipoUva;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_id;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> jSelection_Country;
    private javax.swing.JComboBox<String> jSelection_Country1;
    private javax.swing.JComboBox<String> jSelection_class;
    private javax.swing.JComboBox<String> jSelection_marca;
    private javax.swing.JComboBox<String> jSelection_typegrape;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txt_alcool;
    private javax.swing.JTextField txt_nome;
    private javax.swing.JTextField txt_preco;
    private javax.swing.JTextField txt_safra;
    private javax.swing.JTextField txt_tamanho;
    // End of variables declaration//GEN-END:variables
}
