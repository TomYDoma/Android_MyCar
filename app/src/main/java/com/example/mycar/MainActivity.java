package com.example.mycar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    TextView tvSign;
    LinearLayout main;
    public static TextView tvEmpty, tvBalance;
    EditText etAmount;
    EditText etDate;
    EditText etComment;
    TextView tvMessage;
    static EditText etMessage;
    ImageView ivSend;


    boolean positive = false;
    RecyclerView rvTransactions;
    TransactionAdapter adapter;
    ArrayList<TransactionClass> transactionList;

    //Задаю перечисление вида расходов
    AutoCompleteTextView metMessage;
    final String[] mCats = { "Заправка АИ-95", "Заправка АИ-92", "Заправка АИ-98", "Заправка АИ-100",
            "Мойка", "Шиномонтаж", "Ремонт", "Мелкие расходы"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        metMessage = findViewById(R.id.etMessage);
        metMessage.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, mCats));


        EditText userEditText = findViewById(R.id.etMessage);

        Intent intent = new Intent(MainActivity.this, settings.class);

        intent.putExtra("fuel", userEditText.getText().toString());






        // Function to initialize views
        initViews();

        // Function to load data from shared preferences
        loadData();

        // Function to set custom action bar
        setCustomActionBar();

        // To check if there is no transaction
        checkIfEmpty(transactionList.size());

        // Initializing recycler view
        rvTransactions.setHasFixedSize(false);
        rvTransactions.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TransactionAdapter(this,transactionList);
        rvTransactions.setAdapter(adapter);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        etDate.setText(currentDateTimeString);

        // On click sign change
        tvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSign();
            }
        });



        //Слушатель кнопки отправить
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Проверки ввода
                if(etAmount.getText().toString().trim().isEmpty())
                {
                    etAmount.setError("Вы не указали сумму");
                    return;
                }
                if(etMessage.getText().toString().isEmpty())
                {
                    etMessage.setError("Вы не указали комментарий!");
                    return;
                }
                //Попытка заполнения
                try {
                    int amt = Integer.parseInt(etAmount.getText().toString().trim());

                    String dt = etDate.getText().toString().trim();
                    String cm = etComment.getText().toString().trim();

                    // Adding Transaction to recycler View
                    sendTransaction(amt,etMessage.getText().toString().trim(),dt, cm, positive);
                    //sendTransaction(amt,etDate.getText().toString().trim(), dt, positive);
                    checkIfEmpty(transactionList.size());

                    // To update Balance
                    setBalance(transactionList);
                    etAmount.setText("");
                    etMessage.setText("");
                    etComment.setText("");

                    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                    etDate.setText(currentDateTimeString);
                }
                catch (Exception e){
                    etAmount.setError("Сумма должна быть целым числом, большим нуля!");
                }
            }
        });
    }

    // To set custom action bar
    private void setCustomActionBar() {
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        View v = LayoutInflater.from(this).inflate(R.layout.custom_action_bar,null);

        // TextView to show Balance
        tvBalance = v.findViewById(R.id.tvBalance);

        // Setting balance
        setBalance(transactionList);
        getSupportActionBar().setCustomView(v);
        getSupportActionBar().setElevation(0);
    }

    // To set Balance along with sign (spent(-) or received(+))
    public static void setBalance(ArrayList<TransactionClass> transactionList){
        int bal = calculateBalance(transactionList);
        if(bal<0)
        {
            tvBalance.setText("-"+calculateBalance(transactionList)*-1 + "р");
        }
        else {
            tvBalance.setText("+"+calculateBalance(transactionList) + "р");
        }
    }

    // To load data from shared preference
    private void loadData() {
        SharedPreferences pref = getSharedPreferences("com.cs.ec",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("transactions",null);
        Type type = new TypeToken<ArrayList<TransactionClass>>(){}.getType();
        if(json!=null)
        {
            transactionList=gson.fromJson(json,type);
        }
    }

    // To add transaction
    private void sendTransaction(int amt, String msg, String dt, String cm, boolean positive) {
        transactionList.add(new TransactionClass(amt,msg,dt, cm, positive));
        adapter.notifyDataSetChanged();
        rvTransactions.smoothScrollToPosition(transactionList.size()-1);
    }

    // Функция для изменения знака
    private void changeSign() {
        if(positive)
        {
            tvSign.setText("-");
            tvSign.setTextColor(Color.parseColor("#F44336"));
            positive = false;
        }
        else {
            tvSign.setText("+");
            tvSign.setTextColor(Color.parseColor("#00c853"));
            positive = true;
        }
    }

    // To check if transaction list is empty
    public static void checkIfEmpty(int size) {
        if (size == 0)
        {
            MainActivity.tvEmpty.setVisibility(View.VISIBLE);
        }
        else {
            MainActivity.tvEmpty.setVisibility(View.GONE);
        }
    }

    // To Calculate Balance by iterating through all transactions
    public static int calculateBalance(ArrayList<TransactionClass> transactionList)
    {
        int bal = 0;
        for(TransactionClass transaction : transactionList)
        {
            if(transaction.isPositive())
            {
                bal+=transaction.getAmount();
            }
            else {
                bal-=transaction.getAmount();
            }
        }
        return bal;
    }



    // Initializing Views
    private void initViews() {
        transactionList = new ArrayList<TransactionClass>();
        tvSign = findViewById(R.id.tvSign);
        tvMessage = findViewById(R.id.tvMessage);
        rvTransactions = findViewById(R.id.rvTransactions);
        etAmount = findViewById(R.id.etAmount);
        etMessage = findViewById(R.id.etMessage);
        etComment = findViewById(R.id.etMessage2);
        etDate = findViewById(R.id.etDate);
        ivSend = findViewById(R.id.ivSend);
        tvEmpty = findViewById(R.id.tvEmpty);
    }

    // Storing data locally
    // using shared preferences
    // in onStop() method
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getSharedPreferences("com.cs.ec",MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(transactionList);
        editor.putString("transactions",json);
        editor.apply();
    }

    //Суммирую стоимости кажого вида расходов
    public static int calculateBalanceFuel(ArrayList<TransactionClass> transactionList)
    {
        int countf = 0;
        for(TransactionClass transaction : transactionList)
        {
            if(transaction.getMessage().equals("Заправка АИ-92") ||
                    transaction.getMessage().equals("Заправка АИ-95") ||
                    transaction.getMessage().equals("Заправка АИ-98") ||
                    transaction.getMessage().equals("Заправка АИ-100") ||
                    transaction.getMessage().toLowerCase(Locale.ROOT).equals("заправка"))
            {
                countf = countf + transaction.getAmount();
            }
        }
        return countf;
    }

    public static int calculateBalanceRepair(ArrayList<TransactionClass> transactionList)
    {
        int countr = 0;
        for(TransactionClass transaction : transactionList)
        {
            if(transaction.getMessage().toLowerCase(Locale.ROOT).equals("ремонт"))
            {
                countr = countr + transaction.getAmount();
            }
        }
        return countr;
    }

    public static int calculateBalanceShin(ArrayList<TransactionClass> transactionList)
    {
        int counts = 0;
        for(TransactionClass transaction : transactionList)
        {
            if(transaction.getMessage().toLowerCase(Locale.ROOT).equals("шиномонтаж"))
            {
                counts = counts + transaction.getAmount();
            }
        }
        return counts;
    }

    public static int calculateBalanceWash(ArrayList<TransactionClass> transactionList)
    {
        int countw = 0;
        for(TransactionClass transaction : transactionList)
        {
            if(transaction.getMessage().toLowerCase(Locale.ROOT).equals("мойка"))
            {
                countw = countw + transaction.getAmount();
            }
        }
        return countw;
    }


    public static int calculateBalanceProchee(ArrayList<TransactionClass> transactionList)
    {
        int countp = 0;
        for(TransactionClass transaction : transactionList)
        {
            if(transaction.getMessage().toLowerCase(Locale.ROOT).equals("мелкие расходы"))
            {
                countp = countp + transaction.getAmount();
            }
        }
        return countp;
    }

    //Когда нажимаю на кнопку ИНФО, данные отправляются на вторую активность
    public void onClickSettings(View view) {

        int countf = calculateBalanceFuel(transactionList);
        int countr = calculateBalanceRepair(transactionList);
        int countw = calculateBalanceWash(transactionList);
        int counts = calculateBalanceShin(transactionList);
        int countp = calculateBalanceProchee(transactionList);
        Intent intent = new Intent(MainActivity.this, settings.class);
        intent.putExtra("fuel", countf);
        intent.putExtra("repair", countr);
        intent.putExtra("wash", countw);
        intent.putExtra("shina", counts);
        intent.putExtra("prochee", countp);
        startActivity(intent);
    }
}