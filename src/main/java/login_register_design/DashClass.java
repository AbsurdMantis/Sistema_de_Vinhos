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

public class DashClass extends javax.swing.JFrame {

    public String status_edit = "new";

    //metodo para inserção de novos registos no banco
    private boolean insert(String Classificacao) {
        PreparedStatement st;
        int ur;
        
        //verifica se o campo de texto é vazio
        if (!(Classificacao != null && !Classificacao.isEmpty())) {
            showMessageDialog(null, "Classificação não pode ser vazia!");
            return false;
        }

        //faz a inserção no banco de dados
        try {
            String query = "INSERT INTO vinho_class(class) VALUES (?)";
            st = My_CNX.getConnection().prepareStatement(query);
            st.setString(1, Classificacao);
            ur = st.executeUpdate();
            //retorna se a inserção inseriu linhas, ou não (caso erro ur valerá 0 ou -1 e retornará falso)
            return (ur > 0);
        } catch (SQLException ex) {
            Logger.getLogger(DashClass.class.getName()).log(Level.SEVERE, null, ex);
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
            String query = "DELETE FROM vinho_class WHERE ID = ?";
            st = My_CNX.getConnection().prepareStatement(query);
            st.setInt(1, id);
            ur = st.executeUpdate();
            //retorna se a deleção deletou linhas, ou não (caso erro ur valerá 0 ou -1 e retornará falso)
            return (ur > 0);
        } catch (SQLException ex) {
            Logger.getLogger(DashClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        //retorna falso caso try catch deu errorF
        return false;
    }

    //metodo para fazer o update de Classificacao por ID
    private boolean update(int id, String Classificacao) {
        PreparedStatement st;
        int ur;

        // valida o campo string Classificacao que nao pode ser vazio nem nulo
        if (!(Classificacao != null && !Classificacao.isEmpty())) {
            showMessageDialog(null, "Classificação não pode ser vazia!");
            return false;
        }
        
        //faz o update no banco
        try {
            String query = "UPDATE vinho_class SET class = ? WHERE id = ?";
            st = My_CNX.getConnection().prepareStatement(query);
            st.setString(1, Classificacao);
            st.setInt(2, id);
            ur = st.executeUpdate();
            //retorna se o update atualizou linhas, ou não (caso erro ur valerá 0 ou -1 e retornará falso)
            return (ur > 0);
        } catch (SQLException ex) {
            Logger.getLogger(DashClass.class.getName()).log(Level.SEVERE, null, ex);
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
        String r_Classificacao;
        int r_id;
        
        try {
            String query = "SELECT id, class FROM vinho_class WHERE class like ?";
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
                r_Classificacao = rs.getString("class");
                r_id = rs.getInt("id");
                model.addRow(new Object[]{r_id, r_Classificacao});
            }

        } catch (SQLException ex) {
            Logger.getLogger(DashClass.class.getName()).log(Level.SEVERE, null, ex);
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
                    
                    //quando a linha é selecionada, ele seta os valores para o form (label ID e text field class)
                    int id_selecionado;
                    String Classificacao_selecionada;
                    id_selecionado = Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString());
                    Classificacao_selecionada = jTable1.getValueAt(jTable1.getSelectedRow(), 1).toString();
                    jLabel_id.setText("" + id_selecionado);
                    jTextField_Brand.setText(Classificacao_selecionada);

                    bt_canc.setEnabled(true);
                    bt_del.setEnabled(true);
                    bt_edit.setEnabled(true);
                    bt_save.setEnabled(false);

                }
            }
        });
    }

    public DashClass() {
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
        JTextField_vinhosearch = new javax.swing.JTextField();
        JButton_vinhosearchbutton = new javax.swing.JButton();
        bt_edit = new javax.swing.JButton();
        bt_del = new javax.swing.JButton();
        bt_save = new javax.swing.JButton();
        bt_canc = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel_id = new javax.swing.JLabel();
        jTextField_Brand = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        bt_new = new javax.swing.JButton();
        jButton_opendashvinho = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        JButton_vinhosearchbutton.setText("Buscar");
        JButton_vinhosearchbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButton_vinhosearchbuttonActionPerformed(evt);
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
        jLabel7.setText("Classificação:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Id:");

        jLabel_id.setText("0");

        jTextField_Brand.setEnabled(false);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Classificação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
        }

        bt_new.setText("Nova Classificação");
        bt_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_newActionPerformed(evt);
            }
        });

        jButton_opendashvinho.setText("Dashvinho");
        jButton_opendashvinho.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton_opendashvinhoMouseClicked(evt);
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField_Brand, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(bt_edit)
                                .addGap(30, 30, 30)
                                .addComponent(bt_save)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bt_canc)
                                .addGap(18, 18, 18)
                                .addComponent(bt_del))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(bt_new)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(35, 35, 35))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel_id)
                            .addComponent(jButton_opendashvinho, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel_id)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_Brand, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bt_new, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton_opendashvinho, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(bt_del, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bt_canc, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bt_save, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(bt_edit, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(32, Short.MAX_VALUE))))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Dashboard - Classificação");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 886, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 918, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 633, Short.MAX_VALUE)
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
        bt_new.setEnabled(true);
        status_edit = "canceled";
        JTextField_vinhosearch.setText("");
        jTextField_Brand.setEnabled(false);
        jLabel_id.setText("xx");
        jTextField_Brand.setText("");
    }//GEN-LAST:event_bt_cancActionPerformed

    private void bt_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_editActionPerformed
        // seta o status do painel para o status "editando"
        bt_canc.setEnabled(true);
        bt_del.setEnabled(true);
        bt_edit.setEnabled(false);
        bt_save.setEnabled(true);
        bt_new.setEnabled(false);
        status_edit = "edit";
        JTextField_vinhosearch.setText("");
        jTextField_Brand.setEnabled(true);
    }//GEN-LAST:event_bt_editActionPerformed

    private void bt_delActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_delActionPerformed
        //coloca o panel no modo "deleção"
        bt_canc.setEnabled(false);
        bt_del.setEnabled(false);
        bt_edit.setEnabled(false);
        bt_save.setEnabled(false);
        bt_new.setEnabled(true);
        jTextField_Brand.setEnabled(false);
        JTextField_vinhosearch.setText("");
        status_edit = "deleted";
        // Confirma se o item deve mesmo ser deletado
        int input = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja deletar a Classificacao: " + jTextField_Brand.getText() + "?");
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
        bt_new.setEnabled(true);
        jTextField_Brand.setEnabled(false);
        JTextField_vinhosearch.setText("");
        // como o botão de salvar é o mesmo para "inserir um novo" e "editar um registro existente"
        // é nessa parte que definimos se a operação a se executar é um insert ou update
        // utilizando a variavel status edit que guarda o "status" do painel.
        if (status_edit.equals("new")) {
            //faz a inserção e atualização da tabela se sucesso
            if (insert(jTextField_Brand.getText())) {
                load_table(null);
            }
        } else {
            //faz a atualização da Classificacao e atualização da tabela se sucesso
            if (update(Integer.parseInt(jLabel_id.getText()), jTextField_Brand.getText())) {
                load_table(null);
            }
        }

        status_edit = "saved";
    }//GEN-LAST:event_bt_saveActionPerformed

    private void bt_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_newActionPerformed
        // coloca o panel em status de "cadastrando novo item"
        bt_canc.setEnabled(true);
        bt_del.setEnabled(false);
        bt_edit.setEnabled(false);
        bt_save.setEnabled(true);
        bt_new.setEnabled(false);
        jLabel_id.setText("xx");
        JTextField_vinhosearch.setText("");
        jTextField_Brand.setText("");
        jTextField_Brand.setEnabled(true);
        status_edit = "new";

    }//GEN-LAST:event_bt_newActionPerformed

    private void JButton_vinhosearchbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButton_vinhosearchbuttonActionPerformed
        // carrega a tabela com o parametro de pesquisa presente no text field de pesquisa
        load_table(JTextField_vinhosearch.getText());
    }//GEN-LAST:event_JButton_vinhosearchbuttonActionPerformed

    private void jButton_opendashvinhoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton_opendashvinhoMouseClicked
        Dashvinho lf = new Dashvinho();
        lf.setVisible(true);
        lf.pack();
        lf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.dispose();
    }//GEN-LAST:event_jButton_opendashvinhoMouseClicked

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
            java.util.logging.Logger.getLogger(DashClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DashClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DashClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DashClass().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton JButton_vinhosearchbutton;
    private javax.swing.JTextField JTextField_vinhosearch;
    private javax.swing.JButton bt_canc;
    private javax.swing.JButton bt_del;
    private javax.swing.JButton bt_edit;
    private javax.swing.JButton bt_new;
    private javax.swing.JButton bt_save;
    private javax.swing.JButton jButton_opendashvinho;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel_id;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField_Brand;
    // End of variables declaration//GEN-END:variables
}
