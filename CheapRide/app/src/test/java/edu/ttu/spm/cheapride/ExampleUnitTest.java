package edu.ttu.spm.cheapride;

import android.content.Context;
import android.text.Editable;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {


    @Mock
    private AutoCompleteTextView mEmailView;
    @Mock
    private EditText mPasswordView = Mockito.mock(EditText.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
//    private EditText mPasswordView = Mockito.mock(EditText.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    @Mock
    Context mMockContext;
    @Mock
    LoginActivity mLogin;
//    @Test
//    public void addition_isCorrect() throws Exception {
//        assertEquals(4, 2 + 2);
//    }

    @Test
    public void login_isCorrect() throws Exception {

        mEmailView.setText("cheapridettu.edu");
        mPasswordView.setText("admin");

        when(mLogin.findViewById(R.id.email)).thenReturn(mEmailView);
        when(mLogin.findViewById(R.id.password)).thenReturn(mPasswordView);


        LoginActivity login = new LoginActivity();
        login.attemptLogin();

        String myError = mEmailView.getError().toString();
        assertEquals(myError, "This email address is invalid");

    }
}