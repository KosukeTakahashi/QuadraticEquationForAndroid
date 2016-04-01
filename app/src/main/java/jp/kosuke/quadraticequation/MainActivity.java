package jp.kosuke.quadraticequation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String exp = "二次方程式の解を求めます。\\n{ax² + bx + c = 0}の形で入力してください。\\n解が1つの場合は、同じ値が2つ表示されます。\n各欄には " + Integer.MIN_VALUE + " ～ " + Integer.MAX_VALUE + ") 範囲で入力してください。\nまた、aの欄に0は入力しないでください。";

        final EditText editA = (EditText)findViewById(R.id.editA);
        final EditText editB = (EditText)findViewById(R.id.editB);
        final EditText editC = (EditText)findViewById(R.id.editC);
        final TextView ansV1 = (TextView)findViewById(R.id.ansV1);
        final TextView ansV2 = (TextView)findViewById(R.id.ansV2);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean clean = false;
                //計算処理
                int a = 1;
                int b = 1;
                int c = 1;

                try {
                    a = Integer.parseInt(editA.getText().toString());
                    b = Integer.parseInt(editB.getText().toString());
                    c = Integer.parseInt(editC.getText().toString());
                } catch (NullPointerException e) {
                    Snackbar.make(view, "エラー：NullPointerException:全ての欄に入力してください", Snackbar.LENGTH_SHORT).show();
                    clean = true;
                } catch (NumberFormatException e) {
                    Snackbar.make(view, "エラー：NumberFormatException:値が大きすぎます", Snackbar.LENGTH_SHORT).show();
                    clean = true;
                }

                if (a != 0) {
                    String[] ans = Calc(a, b, c);

                    ansV1.setText(ans[0]);
                    ansV2.setText(ans[1]);

                    if (clean) {
                        ansV1.setText("");
                        ansV2.setText("");
                    }
                } else if (a == 0) {
                    Snackbar.make(view, "aの欄に0が入力されています。aに0は入力しないでください。", Snackbar.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String[] Calc(int a, int b, int c) {
        /*
         *インデックス番号
         *   0 : 分子
         *   1 : 分母
         */

        int w = 2 * a;
        int x = -1 * b;
        int y = b * b;
        int z = -4 * a * c;

        //√の中
        boolean isImaginary = false;
        boolean isSqrted = false;
        boolean isCalculatable = false;
        int outside = 1;
        int inside = 1;
        int yz = y + z;
        if (yz < 0) {
            //√の中は負の数
            //√(x)iの形にする

            yz *= -1;
            isImaginary = true;
        }
        ArrayList<Integer> PF = Calculation.PrimeFactorization(yz);
        if (Calculation.isInt(Math.sqrt((double)yz))) {
            inside = (int)Math.sqrt((double)yz);
            outside = 1;
            isSqrted = true;
        } else if (Calculation.easierRoot(PF, true) == 1) {
            inside = yz;
            outside = 1;
        } else {
            inside = Calculation.easierRoot(PF, false);
            outside = Calculation.easierRoot(PF, true);
        }

        //約分
        int GCD = Calculation.GCD(Calculation.GCD(outside, x), w);  //2a, -b, [√の外]の項の最大公約数を求める

        w /= GCD;
        x /= GCD;
        outside /= GCD;


        //リターン
        String[] ret = new String[2];

        if (inside == 0) {
            ret[0] = String.valueOf(x);
        } else if ((outside == 1) && !isSqrted) {
            ret[0] = x + "±√(" + inside + ")";
        } else if ((outside != 1) && !isSqrted) {
            ret[0] = x + "±" + outside + "√(" + inside + ")";
        } else {
            ret[0] = x + "±" + inside;
            isCalculatable = true;
        }

        ret[1] = String.valueOf(w);

        if (w == 1) {
            ret[1] = "";
        } else if (w < 0) {
            //分母が負の数
            x *= -1;
            ret[1] = String.valueOf(w * (-1));
            ret[0] = x + "±" + outside + "√(" + inside + ")";
        }

        if (Calculation.isInt(Math.sqrt((double)inside))) {
            int root = (int)Math.sqrt((double)inside);
            ret[0] = String.valueOf(x + root) + ", " + String.valueOf(x - root);

            /*
             *インデックス番号
             * 0：左側(x + root)
             * 1：右側(x - root)
             */

            int[] GCDs = new int[2];
            GCDs[0] = Calculation.GCD(w, x + root);
            GCDs[1] = Calculation.GCD(w, x - root);

            //約分
            ret[0] = String.valueOf((x + root) / GCDs[0]) + ", " + String.valueOf((x - root) / GCDs[1]);
            ret[1] = String.valueOf(w / GCDs[0]) + ", " + String.valueOf(w / GCDs[1]);

            if ((w / GCDs[0] == 1) && (w / GCDs[1] == 1)) {
                ret[1] = "";
            }
        }

        if (isCalculatable) {
            int[] bunsi = new int[2];
            bunsi[0] = x + inside;
            bunsi[1] = x - inside;

            int[] GCDs = new int[2];
            GCDs[0] = Calculation.GCD(w, bunsi[0]);
            GCDs[1] = Calculation.GCD(w, bunsi[1]);

            ret[0] = (bunsi[0] / GCDs[0]) + ", " + (bunsi[1] / GCDs[1]);
            ret[1] = (w / GCDs[0]) + ", " + (w / GCDs[1]);

            if ((w / GCDs[0]) < 0) {
                ret[0] = (-1 * (bunsi[0] / GCDs[0])) + ", " + (bunsi[1] / GCDs[1]);
                ret[1] = (-1 * (w / GCDs[0])) + ", " + (w / GCDs[1]);
            } else if ((w / GCDs[1]) < 0) {
                ret[0] = (bunsi[0] / GCDs[0]) + ", " + (-1 * (bunsi[1] / GCDs[1]));
                ret[1] = (w / GCDs[0]) + ", " + (-1 * (w / GCDs[1]));
            } else if (((w / GCDs[1]) < 0) && ((w / GCDs[1]) < 0)) {
                ret[0] = (-1 * (bunsi[0] / GCDs[0])) + ", " + (-1 * (bunsi[1] / GCDs[1]));
                ret[1] = (-1 * (w / GCDs[0])) + ", " + (-1 * (w / GCDs[1]));
            }

            if (ret[1].equals("1, 1")) {
                ret[1] = "";
            }
        }

        if (isImaginary) {
            ret[0] += "i";
        }

        return ret;
    }
}
