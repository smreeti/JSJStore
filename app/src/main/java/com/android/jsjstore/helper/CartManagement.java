package com.android.jsjstore.helper;

public class CartManagement {
//    private Context context;
//    ArrayList<CartDomain> userCartItems = new ArrayList<>();
//
//    public CartManagement(Context context) {
//        this.context = context;
//    }
//
//    public void InsertProduct(CartDomain item){
//        List<CartDomain> items = getListCart();
//        boolean existInCart = false;
//        int n = 0;
//        for (int i = 0; i < items.size(); i ++){
//            if(items.get(i).getProductName().equals((item.getProductName()))){
//                existInCart = true;
//                n = i;
//                break;
//            }
//        }
//
//        CartDatabase appDB = CartDatabase.getInstance(context);
//
//        if(existInCart){
//            CartDomain cartUpdatable = items.get(n);
//            cartUpdatable.setProductQuantity(item.getProductQuantity());
//            appDB.Dao().update(cartUpdatable);
//        }else{
//            this.userCartItems.add(item);
//            appDB.Dao().insert(item);
//            Toast.makeText(context, "Inserting", Toast.LENGTH_SHORT).show();
//        }
//
//        Toast.makeText(context, "Added to Your Cart", Toast.LENGTH_SHORT).show();
//    }
//
//    public void deleteItemFromCart(CartDomain item){
//        CartDatabase appDB = CartDatabase.getInstance(context);
//        appDB.Dao().delete(item);
//        Toast.makeText(context, "Item Deleted From Your Cart", Toast.LENGTH_SHORT).show();
//    }
//
//    public List<CartDomain> getListCart() {
//        //get elements.
//        CartDatabase appDB = CartDatabase.getInstance(context);
//        return appDB.Dao().getAllItems();
//    }
//
//    public void minusNumberItem(List<CartDomain> items, int position, ChangeNumberItemsListener changeNumberItemsListener){
//        if(items.get(position).getProductQuantity() == 1){
//            //items.remove(position);
//            getListCart().remove(position);
//        }else{
//            getListCart().get(position).setProductQuantity(items.get(position).getProductQuantity() -1);
//            //items.get(position).setNumberInCart(items.get(position).getNumberInCart() -1);
//        }
//        changeNumberItemsListener.changed();
//    }
//
//    public void plusNumberItem(List<CartDomain> items, int position, ChangeNumberItemsListener changeNumberItemsListener){
//        getListCart().get(position).setProductQuantity(items.get(position).getProductQuantity() +1);
//        changeNumberItemsListener.changed();
//    }
//
//    public Double getTotalFee(){
//        double fee = 0.00;
//        for(int i = 0; i < getListCart().size(); i++){
//            fee = fee+(getListCart().get(i).getProductUnitPrice() * getListCart().get(i).getProductQuantity());
//        }
//        return fee;
//    }
}
