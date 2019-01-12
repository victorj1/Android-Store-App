package com.example.onlinestoreapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CLIENTS_DATABASE";
    private static final String CLIENTS_TABLE = "CLIENTS_TABLE";
    private static final String PRODUCTS_TABLE = "PRODUCTS_TABLE";
    private static final String ORDERS_TABLE = "ORDERS_TABLE";

    private static int clientsCounter = 1;
    private static int productsCounter = 2;

    private static final String[] CLIENTS_FIELDS = new String[] {
            "userName", "password", "firstName", "lastName", "address",
            "city", "postalCode", "card", "expiryDate"
    };

    private static final String[] PRODUCTS_FIELDS = new String[] {
            "productName", "productCategory", "productPrice",
            "productImage", "productAmount", "productColor"
    };

    private static final String[] ORDERS_FIELDS = new String[] {
            "userName", "orderDate", "orderCost", "deliveryType", "orderProductsAmount"
    };

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + ORDERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CLIENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS CART_TABLE");

        String createClientTable = "CREATE TABLE CLIENTS_TABLE ( " +
                "clientId INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, " +
                "userName TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "email TEXT NOT NULL, " +
                "firstName TEXT NOT NULL, " +
                "lastName TEXT NOT NULL, " +
                "phoneNumber TEXT NOT NULL, " +
                "address TEXT DEFAULT 'No Address', " +
                "city TEXT DEFAULT 'No City', " +
                "postalCode TEXT DEFAULT 'No Postal Code', " +
                "card TEXT NOT NULL, " +
                "expiryDate TEXT NOT NULL );";

        String createProductTable = "CREATE TABLE PRODUCTS_TABLE ( " +
                "productId INTEGER NOT NULL UNIQUE PRIMARY KEY, " +
                "productName TEXT NOT NULL UNIQUE, " +
                "productCategory TEXT NOT NULL, " +
                "productPrice TEXT NOT NULL, " +
                "productImage TEXT NOT NULL, " +
                "productAmount TEXT, " +
                "productColor );";

        String createOrderTable = "CREATE TABLE ORDERS_TABLE ( " +
                "orderId INTEGER NOT NULL UNIQUE PRIMARY KEY, " +
                "orderDate TEXT NOT NULL, " +
                "orderCost TEXT NOT NULL, " +
                "deliveryType TEXT NOT NULL, " +
                "orderProductsAmount TEXT NOT NULL); ";

        String createCartTable = "CREATE TABLE CART_TABLE ( " +
                "transactionId INTEGER NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT, " +
                "userName TEXT NOT NULL, " +
                "productName TEXT NOT NULL, " +
                "amount TEXT NOT NULL); ";

        db.execSQL(createClientTable);
        db.execSQL(createProductTable);
        db.execSQL(createOrderTable);
        db.execSQL(createCartTable);
        db.execSQL(" INSERT INTO CLIENTS_TABLE VALUES (1001, 'admin', 'password', 'victorvjda@gmail.com', 'Victor', 'Jdanovitch', '+16479999999', '941 Progress Ave', 'Toronto', 'M1G3T8', '4536000100012345', '02/23'); ");
        db.execSQL("INSERT INTO PRODUCTS_TABLE VALUES (1, 'Product1', 'category1', '45', 'http://www.recipesaresimple.com/wp-content/uploads/2017/11/Chicken-Karahi-Dhaba-Style-ingredients-640x426.jpg', '15', 'red, blue')");
        db.execSQL("INSERT INTO PRODUCTS_TABLE VALUES (2, 'Product2', 'category1', '35', 'image2', '5', 'red')");
        db.execSQL("INSERT INTO PRODUCTS_TABLE VALUES (3, 'Product3', 'category2', '55', 'image3', '6', 'yellow')");
        db.execSQL("INSERT INTO PRODUCTS_TABLE VALUES (4, 'Product4', 'category2', '15', 'image4', '7', 'green')");
        db.execSQL("INSERT INTO PRODUCTS_TABLE VALUES (5, 'Product5', 'category3', '25', 'image5', '7', 'white')");
        db.execSQL("INSERT INTO PRODUCTS_TABLE VALUES (6, 'Product6', 'category4', '65', 'image6', '2', 'pink')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CLIENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS CART_TABLE");
        onCreate(db);
    }

    public long register(Client client) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("userName", client.userName);
        values.put("password", client.password);
        values.put("email", client.email);
        values.put("firstName", client.firstName);
        values.put("lastName", client.lastName);
        values.put("phoneNumber", client.phoneNumber);
        values.put("address", client.address);
        values.put("city", client.city);
        values.put("postalCode", client.postalCode);
        values.put("card", client.card);
        values.put("expiryDate", client.expiryDate);

        long clientId = -1;
        clientId = db.insert(CLIENTS_TABLE, null, values);
        return clientId;
    }

    public int login(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT clientId FROM " + CLIENTS_TABLE + " WHERE userName = ? AND password = ?", new String[]{ username, password });
        int id = -1;
        Log.i("String", DatabaseUtils.dumpCursorToString(res));
        if( res != null && res.moveToFirst() ){
            id = Integer.parseInt(res.getString(res.getColumnIndex("clientId")));
            res.close();
        }
        return id;
    }

    public Product[] getAllProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" SELECT * FROM " + PRODUCTS_TABLE, null);

        int length = res.getCount();
        Product[] array = new Product[length];

        if (res.moveToFirst()) {
            for (int i = 0; i < length; i++) {
                Product product = new Product();
                product.productName = res.getString(res.getColumnIndex("productName"));
                product.productCategory = res.getString(res.getColumnIndex("productCategory"));
                product.productPrice = res.getString(res.getColumnIndex("productPrice"));
                product.productImage = res.getString(res.getColumnIndex("productImage"));
                product.productColor = res.getString(res.getColumnIndex("productColor"));
                product.productAmount = res.getString(res.getColumnIndex("productAmount"));
                array[i] = product;
                res.moveToNext();
            }
        }
        return array;
    }

    public void decreaseProductAmount(String amount, String productName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + PRODUCTS_TABLE + " SET productAmount = '" + amount + "' WHERE productName = '" + productName + "'; ";
        db.execSQL(sql);
    }

    public void addToCart(String userName, String productName, String amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "INSERT INTO CART_TABLE (userName, productName, amount) VALUES ('" + userName + "', '" + productName + "', '" + amount + "')";
        db.execSQL(sql);
    }

    public Product[] getProductsByType(String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" SELECT * FROM " + PRODUCTS_TABLE + " WHERE productCategory = ?", new String[] { type });

        int length = res.getCount();
        Product[] array = new Product[length];

        if (res.moveToFirst()) {
            for (int i = 0; i < length; i++) {
                Product product = new Product();
                product.productName = res.getString(res.getColumnIndex("productName"));
                product.productCategory = res.getString(res.getColumnIndex("productCategory"));
                product.productPrice = res.getString(res.getColumnIndex("productPrice"));
                product.productImage = res.getString(res.getColumnIndex("productImage"));
                product.productColor = res.getString(res.getColumnIndex("productColor"));
                product.productAmount = res.getString(res.getColumnIndex("productAmount"));
                array[i] = product;
                res.moveToNext();
            }
        }
        return array;
    }

    public Product getProduct(String productName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" SELECT * FROM " + PRODUCTS_TABLE + " WHERE productName = ?", new String[] { productName });

        Product product = new Product();
        if (res.moveToFirst()) {
            product.productName = res.getString(res.getColumnIndex("productName"));
            product.productCategory = res.getString(res.getColumnIndex("productCategory"));
            product.productPrice = res.getString(res.getColumnIndex("productPrice"));
            product.productImage = res.getString(res.getColumnIndex("productImage"));
            product.productColor = res.getString(res.getColumnIndex("productColor"));
            product.productAmount = res.getString(res.getColumnIndex("productAmount"));
        }
        return product;
    }

    public CartItem[] getCart(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" SELECT * FROM CART_TABLE WHERE userName = ?", new String[] { userName });

        int length = res.getCount();
        CartItem[] array = new CartItem[length];
        Log.i("RES", DatabaseUtils.dumpCursorToString(res));
        if (res.moveToFirst()) {
            for (int i = 0; i < length; i++) {
                CartItem item = new CartItem();
                item.productName = res.getString(res.getColumnIndex("productName"));
                item.amount = res.getString(res.getColumnIndex("amount"));
                item.price = getItemPrice(item.productName);
                array[i] = item;
                res.moveToNext();
            }
        }
        return array;
    }

    public int getOrderTotal(String userName) {
        CartItem[] items = getCart(userName);
        int length = items.length;
        int total = 0;
        if (length == 0) return total;
        for (int i = 0; i < length; i++) {
            CartItem item = items[i];
            int cost = Integer.parseInt(item.price) * Integer.parseInt(item.amount);
            total += cost;
        }
        return total;
    }

    public String getItemPrice(String productName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" SELECT productPrice FROM PRODUCTS_TABLE WHERE productName = ?", new String[] { productName });
        String price = "0";
        if (res.moveToFirst()) {
            price = res.getString(res.getColumnIndex("productPrice"));
        }
        return price;
    }

    public void deleteCart(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM CART_TABLE WHERE userName = '" + userName + "'";
        db.execSQL(sql);
    }

    public Client getClient(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + CLIENTS_TABLE + " WHERE username = ?", new String[] { username });

        Client client = new Client();
        if (res.moveToNext()) {
            client.userName = res.getString(res.getColumnIndex("userName"));
            client.password = res.getString(res.getColumnIndex("password"));
            client.firstName = res.getString(res.getColumnIndex("firstName"));
            client.lastName = res.getString(res.getColumnIndex("lastName"));
            client.phoneNumber = res.getString(res.getColumnIndex("phoneNumber"));
            client.email = res.getString(res.getColumnIndex("email"));
            client.address = res.getString(res.getColumnIndex("address"));
            client.city = res.getString(res.getColumnIndex("city"));
            client.postalCode = res.getString(res.getColumnIndex("postalCode"));
            client.card = res.getString(res.getColumnIndex("card"));
            client.expiryDate = res.getString(res.getColumnIndex("expiryDate"));
        }

        return client;
    }

    public void addOrder(String username, String orderDate, String orderCost, String deliveryType, String orderProductsAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues orderValues = new ContentValues();
        orderValues.put(ORDERS_FIELDS[1], orderDate);
        orderValues.put(ORDERS_FIELDS[2], orderCost);
        orderValues.put(ORDERS_FIELDS[3], deliveryType);
        orderValues.put(ORDERS_FIELDS[4], orderProductsAmount);
        db.insert(ORDERS_TABLE, null, orderValues);
    }

    public Order[] getOrdersByUserName(String userName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(" SELECT * FROM " + ORDERS_TABLE + " WHERE userName = ?", new String[] { userName });

        int length = res.getCount();
        Order[] array = new Order[length];

        if (res.moveToFirst() && res.moveToNext()) {
            for (int i = 0; i < length; i++) {
                Order order = new Order();
                order.orderDate = res.getString(res.getColumnIndex("orderDate"));
                order.orderCost = res.getString(res.getColumnIndex("orderCost"));
                order.orderProductsAmount = res.getString(res.getColumnIndex("orderProductsAmount"));
            }
        }
        return array;
    }
}
