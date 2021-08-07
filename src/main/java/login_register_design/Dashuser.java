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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Dashuser extends javax.swing.JFrame {

    public String status_edit = "new";

    //metodo para deletar registros por id
    private boolean delete(int id) {
        PreparedStatement st;
        int ur;

        //faz a deleção no banco
        try {
            String query = "DELETE FROM users WHERE ID = ?";
            st = My_CNX.getConnection().prepareStatement(query);
            st.setInt(1, id);
            ur = st.executeUpdate();
            //retorna se a deleção deletou linhas, ou não (caso erro ur valerá 0 ou -1 e retornará falso)
            return (ur > 0);
        } catch (SQLException ex) {
            Logger.getLogger(Dashuser.class.getName()).log(Level.SEVERE, null, ex);
        }
        //retorna falso caso try catch deu errorF
        return false;
    }

    //metodo para fazer o update de param por ID
    private boolean update(int id, String full_name, String username, String password, int phone, String gender) {
        PreparedStatement st;
        int ur;

// valida o campo string marca que nao pode ser vazio nem nulo
        if (!(full_name != null && !full_name.isEmpty())) {
            showMessageDialog(null, "Nome não pode ser vazia!");
            return false;
        }// valida o campo string marca que nao pode ser vazio nem nulo
        if (!(username != null && !username.isEmpty())) {
            showMessageDialog(null, "Nome de usuário não pode ser vazia!");
            return false;
        }// valida o campo string marca que nao pode ser vazio nem nulo
        if (!(password != null && !password.isEmpty())) {
            showMessageDialog(null, "Senha não pode ser vazia!");
            return false;
        }// valida o campo string marca que nao pode ser vazio nem nulo
        if (phone <= 0) {
            showMessageDialog(null, "Telefone não pode ser vazia!");
            return false;
        }// valida o campo string marca que nao pode ser vazio nem nulo
        if (!(gender != null && !gender.isEmpty())) {
            showMessageDialog(null, "Gênero não pode ser vazia!");
            return false;
        }

        //faz o update no banco
        try {
            String query = "UPDATE users SET full_name = ?, username = ?, password = ?, phone = ?, gender = ? WHERE id = ?";
            st = My_CNX.getConnection().prepareStatement(query);
            st.setString(1, full_name);
            st.setString(2, username);
            st.setString(3, password);
            st.setInt(4, phone);
            st.setString(5, gender);
            st.setInt(6, id);
            ur = st.executeUpdate();
            //retorna se o update atualizou linhas, ou não (caso erro ur valerá 0 ou -1 e retornará falso)
            return (ur > 0);
        } catch (SQLException ex) {
            Logger.getLogger(DashMarca.class.getName()).log(Level.SEVERE, null, ex);
        }
        //retorna falso caso try catch deu error
        return false;
    }

    // faz a atualização dos registros na tabela, ou seja
    // faz um select no banco de dados e coloca o retorno dele na tabela
    // possui um parametro string que é utilizado no where em formato de "like"
    private void load_table(String where) {
        PreparedStatement st;
        ResultSet rs;
        String full_name;
        String username;
        String password;
        int phone;
        String gender;
        Boolean admin;

        int r_id;

        try {
            String query = "SELECT id, full_name, username, password, phone, gender, admin FROM users WHERE full_name like ?";
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
                full_name = rs.getString("full_name");
                username = rs.getString("username");
                password = rs.getString("password");
                phone = rs.getInt("phone");
                gender = rs.getString("gender");
                r_id = rs.getInt("id");
                admin = rs.getBoolean("admin");
                model.addRow(new Object[]{r_id, full_name, username, password, phone, gender, admin});
            }

        } catch (SQLException ex) {
            Logger.getLogger(Dashuser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //initCustom metodoo que é executado após o initCoomponents
    // está aqui para adicionar um listener na tabela, para que sempre que uma
    // seleção ocorra em alguma linha, ele jogue os dados para o formulario de edição
    private void initCustom() {
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {

                if (jTable1.getSelectedRow() > -1) {

                    //quando a linha é selecionada, ele seta os valores para o form (label ID e text field brand)
                    int id_selecionado;
                    String full_name_selecionado;
                    String username_selecionado;
                    String password_selecionado;
                    int phone_selecionado;
                    String gender_selecionado;
                    

                    id_selecionado = Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString());
                    full_name_selecionado = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();
                    username_selecionado = jTable1.getValueAt(jTable1.getSelectedRow(), 2).toString();
                    password_selecionado = jTable1.getValueAt(jTable1.getSelectedRow(), 3).toString();
                    phone_selecionado = Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 4).toString());
                    gender_selecionado = jTable1.getValueAt(jTable1.getSelectedRow(), 5).toString();

                    jLabel_id.setText("" + id_selecionado);
                    jTextField_fullname.setText(full_name_selecionado);
                    jTextField_username.setText(username_selecionado);
                    jTextField_password.setText(password_selecionado);
                    jTextField_phone.setText(String.valueOf(phone_selecionado));
                    jTextField_gender.setText(gender_selecionado);

                    bt_canc.setEnabled(true);
                    bt_del.setEnabled(true);
                    bt_edit.setEnabled(true);
                    bt_save.setEnabled(false);

                }
            }
        });
    }

    public Dashuser() {
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        JTextField_search = new javax.swing.JTextField();
        JButton_searchbutton = new javax.swing.JButton();
        bt_edit = new javax.swing.JButton();
        bt_del = new javax.swing.JButton();
        bt_save = new javax.swing.JButton();
        bt_canc = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel_id = new javax.swing.JLabel();
        jTextField_fullname = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jTextField_username = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField_password = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField_phone = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField_gender = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton_opendashvinho = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        JButton_searchbutton.setText("Buscar");
        JButton_searchbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButton_searchbuttonActionPerformed(evt);
            }
        });

        bt_edit.setText("Editar");
        bt_edit.setEnabled(false);
        bt_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_editActionPerformed(evt);
            }
        });

        bt_del.setText("Deletar");
        bt_del.setEnabled(false);
        bt_del.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_delActionPerformed(evt);
            }
        });

        bt_save.setText("Salvar");
        bt_save.setEnabled(false);
        bt_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_saveActionPerformed(evt);
            }
        });

        bt_canc.setText("Cancelar");
        bt_canc.setEnabled(false);
        bt_canc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_cancActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Nome Completo:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Id:");

        jLabel_id.setText("0");

        jTextField_fullname.setEnabled(false);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nome Completo", "Username", "Senha", "Telefone", "Gênero", "Admin"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jTextField_username.setEnabled(false);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Nome de Usuário:");

        jTextField_password.setEnabled(false);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Senha:");

        jTextField_phone.setEnabled(false);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Telefone:");

        jTextField_gender.setEnabled(false);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Gênero:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(JTextField_search, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButton_searchbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel_id))
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField_fullname, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(bt_edit)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(bt_save)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(bt_canc)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bt_del)
                                .addGap(0, 4, Short.MAX_VALUE))
                            .addComponent(jTextField_username, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField_password, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField_phone, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField_gender, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(35, 35, 35))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField_fullname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField_username, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_password, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField_gender, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bt_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_save, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_canc, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_del, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(JTextField_search, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JButton_searchbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel_id))
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Dashboard - Users");

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
                .addGap(0, 13, Short.MAX_VALUE))
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

        jButton_opendashvinho.setText("Voltar");
        jButton_opendashvinho.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_opendashvinhoMouseClicked(evt);
            }
        });
        jButton_opendashvinho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_opendashvinhoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(798, Short.MAX_VALUE)
                .addComponent(jButton_opendashvinho, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jButton_opendashvinho, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(539, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bt_cancActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_cancActionPerformed
        // seta o status do painel para status "cancelado"
        bt_canc.setEnabled(false);
        bt_del.setEnabled(false);
        bt_edit.setEnabled(false);
        bt_save.setEnabled(false);
        status_edit = "canceled";
        JTextField_search.setText("");
        jTextField_fullname.setEnabled(false);
        jTextField_username.setEnabled(false);
        jTextField_password.setEnabled(false);
        jTextField_phone.setEnabled(false);
        jTextField_gender.setEnabled(false);
        jLabel_id.setText("xx");
        jTextField_fullname.setText("");
        jTextField_username.setText("");
        jTextField_password.setText("");
        jTextField_phone.setText("");
        jTextField_gender.setText("");
    }//GEN-LAST:event_bt_cancActionPerformed

    private void bt_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_editActionPerformed
        // seta o status do painel para o status "editando"
        bt_canc.setEnabled(true);
        bt_del.setEnabled(true);
        bt_edit.setEnabled(false);
        bt_save.setEnabled(true);
        status_edit = "edit";
        JTextField_search.setText("");
        jTextField_fullname.setEnabled(true);
        jTextField_username.setEnabled(true);
        jTextField_password.setEnabled(true);
        jTextField_phone.setEnabled(true);
        jTextField_gender.setEnabled(true);

    }//GEN-LAST:event_bt_editActionPerformed

    private void bt_delActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_delActionPerformed
        //coloca o panel no modo "deleção"
        bt_canc.setEnabled(false);
        bt_del.setEnabled(false);
        bt_edit.setEnabled(false);
        bt_save.setEnabled(false);
        jTextField_fullname.setEnabled(false);
        JTextField_search.setText("");
        status_edit = "deleted";
        // Confirma se o item deve mesmo ser deletado
        int input = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar a marca: " + jTextField_fullname.getText() + "?");
        if (input != 0) {
            // se a deleção for cancelada, dispara o medoto do botão cancelar
            bt_cancActionPerformed(null);
        } else {
            //se não, deleta o item e faz a atualização dos itens na tabela
            if (delete(Integer.parseInt(jLabel_id.getText()))) {
                load_table(null);
            }
        }
    }//GEN-LAST:event_bt_delActionPerformed

    private void bt_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_saveActionPerformed
        bt_canc.setEnabled(false);
        bt_del.setEnabled(false);
        bt_edit.setEnabled(false);
        bt_save.setEnabled(false);
        jTextField_fullname.setEnabled(false);
        jTextField_username.setEnabled(false);
        jTextField_password.setEnabled(false);
        jTextField_phone.setEnabled(false);
        jTextField_gender.setEnabled(false);
        JTextField_search.setText("");
        
        // como o botão de salvar é o mesmo para "inserir um novo" e "editar um registro existente"
        // é nessa parte que definimos se a operação a se executar é um insert ou update
        // utilizando a variavel status edit que guarda o "status" do painel.
        //faz a atualização da marca e atualização da tabela se sucesso
        //faz a atualização da marca e atualização da tabela se sucesso
        //int id, String full_name, String username, String password, int phone, String gender
        if (update(Integer.parseInt(jLabel_id.getText()),
                jTextField_fullname.getText(),
                jTextField_username.getText(),
                jTextField_password.getText(),
                Integer.parseInt(jTextField_phone.getText()),
                jTextField_gender.getText())) {
            load_table(null);
        }

        status_edit = "saved";
    }//GEN-LAST:event_bt_saveActionPerformed

    private void JButton_searchbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButton_searchbuttonActionPerformed
        // carrega a tabela com o parametro de pesquisa presente no text field de pesquisa
        load_table(JTextField_search.getText());
    }//GEN-LAST:event_JButton_searchbuttonActionPerformed

    private void jButton_opendashvinhoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton_opendashvinhoMouseClicked
        // goto Dashboard_Vinho:
        Menu_Form rf = new Menu_Form();
        rf.setVisible(true);
        rf.pack();
        rf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.dispose();
    }//GEN-LAST:event_jButton_opendashvinhoMouseClicked

    private void jButton_opendashvinhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_opendashvinhoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton_opendashvinhoActionPerformed

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
            java.util.logging.Logger.getLogger(Dashuser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashuser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashuser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashuser.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashuser().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButton_searchbutton;
    private javax.swing.JTextField JTextField_search;
    private javax.swing.JButton bt_canc;
    private javax.swing.JButton bt_del;
    private javax.swing.JButton bt_edit;
    private javax.swing.JButton bt_save;
    private javax.swing.JButton jButton_opendashvinho;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_id;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField_fullname;
    private javax.swing.JTextField jTextField_gender;
    private javax.swing.JTextField jTextField_password;
    private javax.swing.JTextField jTextField_phone;
    private javax.swing.JTextField jTextField_username;
    // End of variables declaration//GEN-END:variables
}
