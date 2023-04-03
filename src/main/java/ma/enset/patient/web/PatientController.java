package ma.enset.patient.web;

import lombok.AllArgsConstructor;
import ma.enset.patient.entities.Patient;
import ma.enset.patient.repositories.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Controller
public class PatientController {
    private PatientRepository patientRepository;

    @GetMapping(path = "/index")

    public String patients(Model model,
                          @RequestParam (name = "page",defaultValue = "0")int page ,
                           @RequestParam (name = "size",defaultValue = "5")  int size,
                          @RequestParam (name = "keyword",defaultValue = "")  String keyword)
    {
        //file html patients.html
        Page<Patient> Pagepatients=patientRepository.findByNomContains(keyword,PageRequest.of(page,
                size));

        model.addAttribute("ListPatients",Pagepatients.getContent());
          model.addAttribute("pages",new int [Pagepatients.getTotalPages()]);
          model.addAttribute("currentpage",page);
        model.addAttribute("keyword",keyword);
        return "patients";
    }
    @GetMapping("/delete")
    public String delete(long id,String keyword, int page){

     patientRepository.deleteById(id);
     return "redirect:/index?page="+page+"&keyword="+keyword;

    }
    @GetMapping("/")
    public String home(){

        return "redirect:/index";

    }
    @GetMapping("/patients")
    @ResponseBody
    public List<Patient> ListPatients(){
        return patientRepository.findAll();

    }
    @GetMapping("/formPatients")
    public String formPatients(Model model){

        model.addAttribute("patient",new Patient());
        return "formPatients";
    }

    @PostMapping(path = "/save")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,@RequestParam(defaultValue = "0")  int page, @RequestParam(defaultValue = "") String keyword){
        if(bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient);
        return "redirect:/index?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/editPatients")
    public String editePatients(Model model,Long id,String keyword,int page){
        Patient patient=patientRepository.findById(id).orElse(null);
if (patient==null)throw new RuntimeException("Patient introuvable");
        model.addAttribute("patient",patient);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        return "editPatients";
    }

}
