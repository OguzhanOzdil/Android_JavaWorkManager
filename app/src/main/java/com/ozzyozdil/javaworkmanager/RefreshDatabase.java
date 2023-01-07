package com.ozzyozdil.javaworkmanager;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class RefreshDatabase extends Worker {

    Context myContext;

    // Constructor
    public RefreshDatabase(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.myContext = context;
    }

    // doWork
    @NonNull
    @Override
    public Result doWork() {

        // İşi burada yapın - bu durumda, görüntüleri yükleyin.
        Data data = getInputData();  // main activity veri almak için kullanıyoruz
        int activityNumber = data.getInt("intKey", 0);

        refreshDatabase(activityNumber);

        // İşin Sonuç ile başarılı bir şekilde tamamlanıp tamamlanmadığını belirtin
        return Result.success();
    }

    private void refreshDatabase(int activityNumber){
        // Activity sınıfında olmadığımız için this kullanamıyoruz
        SharedPreferences sharedPreferences = myContext.getSharedPreferences("com.ozzyozdil.javaworkmanager", Context.MODE_PRIVATE);
        int myNumber = sharedPreferences.getInt("number", 0);
        myNumber += activityNumber;
        System.out.println(myNumber);
        sharedPreferences.edit().putInt("number", myNumber).apply();
    }
}
