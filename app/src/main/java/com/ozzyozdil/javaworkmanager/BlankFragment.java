package com.ozzyozdil.javaworkmanager;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BlankFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Data data = new Data.Builder().putInt("intKey", 1).build();  // workmanager sınıfına veri göndermek için kullanıyoruz

        Constraints constraints = new Constraints.Builder()       // WorkManager ın çalışma şartlarını belirliyoruz
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(true)
                .build();

        // OneTimeWorkRequest
        /*WorkRequest workRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
                .setConstraints(constraints)   // oluşturduğumuz kısıtlama varsa bu şekilde ekliyoruz
                .setInputData(data)            // veri varsa bu şekilde
                .setInitialDelay(3, TimeUnit.SECONDS)  // Uygulama açıldıktan kaç zaman sonra workmanager çalışsın var Örn: 5 ay sonra - 10 saniye sonra - 3 saat sonra vs.
                .addTag("myTag")               // birden fazla workrequest var ise tag ler ile ayırt edebiliriz
                .build();*/

        // PeriodicWorkRequest
        WorkRequest workRequest = new PeriodicWorkRequest.Builder(RefreshDatabase.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .setInputData(data)
                .build();


        // Fragment olduğu için this yerine requireContext() kullanıyoruz
        WorkManager.getInstance(requireContext()).enqueue(workRequest);   // Work request i çalıştırmak için enqueue() kullanıyoruz

        // WorkManager ın ne durumda olduğunu gözlemleyebiliriz
        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(workRequest.getId()).observe(requireActivity(), new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {

                if (workInfo.getState() == WorkInfo.State.RUNNING){
                    System.out.println("Running...");
                }
                else if (workInfo.getState() == WorkInfo.State.SUCCEEDED){
                    System.out.println("Succeded.");
                    Toast.makeText(requireContext(), "Succeded", Toast.LENGTH_SHORT).show();
                }
                else if (workInfo.getState() == WorkInfo.State.FAILED){
                    System.out.println("Failed");
                }
            }
        });

        // WorkManager ı iptal etmek için kullanılır
        // WorkManager.getInstance(requireContext()).cancelAllWork();

        /*
        // Chaining
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(RefreshDatabase.class)
                .setConstraints(constraints)
                .setInputData(data)
                .build();

        WorkManager.getInstance(requireContext()).beginWith(oneTimeWorkRequest)
                .then(oneTimeWorkRequest)
                .then(oneTimeWorkRequest)
                .enqueue();
        */
    }
}