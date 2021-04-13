package actions;

import com.opensymphony.xwork2.ActionProxy;
import facade.FacadeParis;
import modele.Match;
import modele.Pari;
import modele.Utilisateur;
import org.apache.struts2.StrutsJUnit4TestCase;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class TestParier extends StrutsJUnit4TestCase {



    @Test
    public void testOk() throws Exception {

         long idMatch= 1;
         long idPari = 1;
         String pseudo = "Zak";


         double montant=200;
         request.addParameter("montant", "200,0");

         String resultat="null";
         request.addParameter("resultat",resultat);

         request.addParameter("idMatch", String.valueOf(idMatch));


        Map<String,Object> sessions = new HashMap<>();
        Map<String,Object> applications = new HashMap<>();

        ActionProxy actionProxy = getActionProxy("/parier");
        actionProxy.getInvocation().getInvocationContext().setApplication(applications);
        actionProxy.getInvocation().getInvocationContext().setSession(sessions);


        FacadeParis facadeParis = EasyMock.createMock(FacadeParis.class);
        Match match = EasyMock.createMock(Match.class);
        Utilisateur utilisateur = EasyMock.createMock(Utilisateur.class);
        Pari pari = EasyMock.createMock(Pari.class);




        EasyMock.expect(facadeParis.getMatch(idMatch)).andReturn(match);
        EasyMock.expect(facadeParis.parier(pseudo,idMatch,resultat,montant)).andReturn(idPari);
        EasyMock.expect(utilisateur.getLogin()).andReturn(pseudo);
        EasyMock.expect(match.getEquipe1()).andReturn("Om");
        EasyMock.expect(match.getEquipe2()).andReturn("Psg");
        EasyMock.expect(pari.getMatch()).andReturn(match);
        EasyMock.expect(facadeParis.getPari(idPari)).andReturn(pari);


        EasyMock.replay(facadeParis,match,utilisateur,pari);


        sessions.put("user",utilisateur);
        applications.put("facade",facadeParis);

        String resultat1 = actionProxy.execute();

        Parier actionExecutee = (Parier) actionProxy.getAction();

        Assert.assertEquals(resultat1,"success");

       // Assert.assertTrue(actionExecutee.getPari()==idMatch);
        Assert.assertTrue(actionExecutee.getMatch()==match);




    }

    @Test
    public void testKoMontantInferieur() throws Exception {

        long idMatch= 45;
        long idPari = 1;
        String pseudo = "Zak";


        double montant=200;
        request.addParameter("montant", "200,0");

        String resultat="null";
        request.addParameter("resultat",resultat);

        request.addParameter("idMatch", String.valueOf(idMatch));


        Map<String,Object> sessions = new HashMap<>();
        Map<String,Object> applications = new HashMap<>();

        ActionProxy actionProxy = getActionProxy("/parier");
        actionProxy.getInvocation().getInvocationContext().setApplication(applications);
        actionProxy.getInvocation().getInvocationContext().setSession(sessions);


        FacadeParis facadeParis = EasyMock.createMock(FacadeParis.class);
        EasyMock.expect(facadeParis.getMatch(idMatch)).andReturn(null);



        EasyMock.replay(facadeParis);


        applications.put("facade",facadeParis);

        String resultat1 = actionProxy.execute();




        Assert.assertEquals(resultat1,"input");




    }

    @Test
    public void testKoMatchInexistant() throws Exception {

        long idMatch= 45;
        long idPari = 1;
        String pseudo = "Zak";


        double montant=200;
        request.addParameter("montant", "200,0");

        String resultat="null";
        request.addParameter("resultat",resultat);

        request.addParameter("idMatch", String.valueOf(idMatch));


        Map<String,Object> sessions = new HashMap<>();
        Map<String,Object> applications = new HashMap<>();

        ActionProxy actionProxy = getActionProxy("/parier");
        actionProxy.getInvocation().getInvocationContext().setApplication(applications);
        actionProxy.getInvocation().getInvocationContext().setSession(sessions);


        FacadeParis facadeParis = EasyMock.createMock(FacadeParis.class);
        EasyMock.expect(facadeParis.getMatch(idMatch)).andReturn(null);



        EasyMock.replay(facadeParis);


        applications.put("facade",facadeParis);





        Parier action= (Parier)  actionProxy.getAction();
        String resultat1 = actionProxy.execute();



        Assert.assertEquals(resultat1,"input");
        if (action.getFieldErrors().get("idMatch").size()>0){

            Assert.assertEquals("match inexistant.",action.getFieldErrors().get("idMatch").get(0));
        }else {

            Assert.fail("Zebi tnaket, j'ai pas trouve le message d'erreur pour idMatch");
        }



    }
}
