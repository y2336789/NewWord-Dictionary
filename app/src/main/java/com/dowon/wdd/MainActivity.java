package com.dowon.wdd;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;

    private Fragment fa;

    public Komoran komoran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        komoran = new Komoran(DEFAULT_MODEL.LIGHT);
        komoran.setUserDic("/data/data/com.dowon.wdd/dic.user");

        //평소에는 화면의 한쪽에 숨겨져 있다가 사용자가 액션을 취하면 화면에 나타날 수 있도록 하는 레이아웃 (activity_main.xml에서 설정됨)
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        //왼쪽위 메뉴 버튼을 클릭하면
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // start에 지정된 Drawer 열기 (activity_main.xml 의 NavigaitionView 부분에 start로 설정함.)
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        //실제로 숨겨졌다가 나오는 뷰 그 자체를말한다.
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        //네비 컨트롤러를 가져오는 코드, 설정
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        final TextView textTitle = findViewById(R.id.textTitle); // 텍스트 타이틀 (신조어사전 부분)

        InputStream is = null;
        FileOutputStream fos = null;
        
        //dic.user파일을 핸드폰에 삽입시키기, 이미 dic.user가 존재할경우 새로운 내용으로 업데이트
        try {
            is = getAssets().open("dic.user");
            int size = is.available();
            byte[] buffer = new byte[size];
            File outfile = new File("/data/data/com.dowon.wdd/dic.user");
            fos = new FileOutputStream(outfile, false);
            for (int c = is.read(buffer); c != -1; c = is.read(buffer)) {
                fos.write(buffer, 0, c);
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //리스너 등록 후 탐색을 인식.
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                textTitle.setText(destination.getLabel());
            }
        });
    }
    //트랜잭션 실행, 플래그먼트매니저로 플레그먼트 트랜잭션을 만듬 -> replace 사용. (플래그먼트를 replace)
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.navHostFragment, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }
}

//        파이어베이스 연동하고 컬렉션에 문서 저장하기
//        Word word = new Word("hi","무야호","몰라");
//        db.collection("word").document("2")
//                .set(word)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d("check3", "저장 성공");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("check3", "저장 실패");
//                    }
//                });

//        사용자 지정 사전 없이 실행할 수 있도록 하는 코드
//        FileOutputStream fos;
//
//        String strFileContents = "핼로우";
//
//        try {
//            fos = openFileOutput("dic2.user", MODE_PRIVATE);
//            fos.write(strFileContents.getBytes());
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }