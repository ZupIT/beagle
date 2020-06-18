s/br.com.zup.beagle.sample.micronaut/br.com.zup.beagle.sample.spring/;
s/br.com.zup.beagle.micronaut/br.com.zup.beagle.spring/g;

s/javax.inject.Singleton/org.springframework.stereotype.Service/;
s/io.micronaut.http.annotation.Controller/org.springframework.web.bind.annotation.RestController/;
s/io.micronaut.http.annotation.Post/org.springframework.web.bind.annotation.PostMapping/;
s/io.micronaut.http.annotation.Get/org.springframework.web.bind.annotation.GetMapping/;
s/io.micronaut.http.annotation.Put/org.springframework.web.bind.annotation.PutMapping/;
s/io.micronaut.http.annotation.Delete/org.springframework.web.bind.annotation.DeleteMapping/;

s/@Singleton/@Service/g;
s/@Controller/@RestController/g;
s/@Get/@GetMapping/g;
s/@Post/@PostMapping/g;
s/@Put/@PutMapping/g;
s/@Delete/@DeleteMapping/g;