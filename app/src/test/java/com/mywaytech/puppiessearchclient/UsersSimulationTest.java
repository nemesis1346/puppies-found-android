package com.mywaytech.puppiessearchclient;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.mywaytech.puppiessearchclient.models.NewUserModel;
import com.mywaytech.puppiessearchclient.models.ReportModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by nemesis1346 on 2/3/2017.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)

public class UsersSimulationTest {

    private NewUserModel currentUserModel;
    private ReportModel currentReportModel;
    private List<NewUserModel> models;
    private List<String> namesResources;
    private List<String> lastNameResources;
    private List<Integer> passwordResources;
    private List<String> commentsResources;
    private List<String> emailResources;
    private List<String> addressResources;


    @Test
    public void date_isCorrect() throws Exception {

        String finalResult = "{";

        ShadowLog.setupLogging();
        ShadowLog.stream = System.out;
        //FIRST TEST

        Random r = new Random();
        int counterRandom1;
        int counterRandom2;
        int counterRandom3;
        int counterRandom4;
        int passCounter1;
        int passCounter2;
        int passCounter3;
        int passCounter4;
        int passCounter5;
        int passCounter6;
        int emailCounter;
        int addressCounter1;
        int addressCounter2;
        int imageCounter;

        models = new ArrayList<>();
        namesResources = Arrays.asList("Marco", "Antonio", "Daniel", "Andres", "Ignacio", "Ronald", "Jose", "William", "Miguel", "Mauricio", "Ruben", "Edison", "Leonardo", "Luis", "Cristian", "Fernando", "Vagner", "Danielo", "Daniel", "Oswaldo", "Pedro", "Diego", "Zack", "Inti", "Huascar");
        lastNameResources = Arrays.asList("Maigua", "Teran", "Molina", "Zambrano", "Altamirano", "Cartagenova", "Lema", "Quimbo", "Cabascango", "Meneses", "Miranda", "Aguilar", "Cachiguango", "Jackson", "Males", "Sanchez", "Chango", "Mosquera", "Amay", "Mendoza");
        passwordResources = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        emailResources = Arrays.asList("gmail", "hotmail", "yahoo", "outlook");
        addressResources=Arrays.asList("Av. América","Av. Amazonas","Juan Leon Mera","Jeronimo Carrion","Bolivar","Morales","Quito","Colon","Av. 6 de diciembre","12 de Octubre","Vicentina","9 de Octubre","Av. Naciones Unidas","Plaza Artigas");

        commentsResources = Arrays.asList("Hola, quisiera saber ¿Cómo lo puedo adoptar? ¿Debo llenar algún formulario o algo?", "Qué lindo, espero encuentre un hogar pronto.", "Su nombre es Toby busca un hogar donde le brinden mucho amor y mucho tiempo para ser feliz", "Cahorrita para adopción responsable", "Perrita en adopción se llama Sheyla se lleva muy bien con los niños", "Molly tiene dos años y quiere un hogar, le gustan mucho los niños", "Me llamo Richi busco un dueño responsable que me ame mucho tengo 4 años", "Mi perrita panda necesita un nuevo amigo para pasear alguien???", "Que buena aplicación mi perro por fin podrá conseguir nuevos amigos está muy solo", "Lucas mira hay muchos perritos que podemos conocer", "Tengo un gato y un perro alguien quiere conocerlos", "Este es Angelito y necesita un dueño responsable.", "Adoptame, soy Scooby doo soy muy juguetón y cariñoso, busco alguien que me quiera.", "Mi perrito está un poco decaído, alguien sabe que síntomas son los que tengo que ver para ver si está enfermo??");

        //JUST FOR MALE USERS
        for (int i = 0; i < 50; i++) {
            counterRandom1 = r.nextInt(namesResources.size());
            counterRandom2 = r.nextInt(namesResources.size());
            counterRandom3 = r.nextInt(lastNameResources.size());
            counterRandom4 = r.nextInt(lastNameResources.size());

            passCounter1 = r.nextInt(passwordResources.size());
            passCounter2 = r.nextInt(passwordResources.size());
            passCounter3 = r.nextInt(passwordResources.size());
            passCounter4 = r.nextInt(passwordResources.size());
            passCounter5 = r.nextInt(passwordResources.size());
            passCounter6 = r.nextInt(passwordResources.size());

            emailCounter = r.nextInt(emailResources.size());

            addressCounter1 = r.nextInt(addressResources.size());
            addressCounter2=r.nextInt(addressResources.size());

            currentUserModel = new NewUserModel();
            currentUserModel.setmName(namesResources.get(counterRandom1) + " " + namesResources.get(counterRandom2) + " " + lastNameResources.get(counterRandom3) + " " + lastNameResources.get(counterRandom4));
            currentUserModel.setmPassword(String.valueOf(passwordResources.get(passCounter1)) + String.valueOf(passwordResources.get(passCounter2)) + String.valueOf(passwordResources.get(passCounter3)) + String.valueOf(passwordResources.get(passCounter4)) + String.valueOf(passwordResources.get(passCounter5)) + String.valueOf(passwordResources.get(passCounter6)));
            currentUserModel.setmEmail(namesResources.get(counterRandom1) + "." + lastNameResources.get(counterRandom3) + "@" + emailResources.get(emailCounter) + ".com");
            currentUserModel.setAddress(addressResources.get(addressCounter1)+" y "+addressResources.get(addressCounter2));
            currentUserModel.setUid("TEST_"+UUID.randomUUID().toString());
            currentUserModel.setUserImagePath("userPicturetestImage_" + i + ".jpg");
            Log.i("name: ", "" + currentUserModel.getmName());
            Log.i("password: ", "" + currentUserModel.getmPassword());
            Log.i("email: ", "" + currentUserModel.getmEmail());
            Log.i("address: ", "" + currentUserModel.getmAddress());
            Log.i("uid: ", "" + currentUserModel.getmUid());
            Log.i("image: ", "" + currentUserModel.getmUserImagePath());
            Gson gson = new Gson();
            String jsonInString = gson.toJson(currentUserModel);
            String result ="\""+ currentUserModel.getmUid() + "\"" + ":" + jsonInString;
            Log.i("json: ", "" + result);
            finalResult += result+",";
        }
        finalResult += "}";
        Log.i("finalJson: ", "" + finalResult);

    }
}