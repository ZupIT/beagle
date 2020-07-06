s/br.com.zup.beagle.sample.spring/br.com.zup.beagle.sample.micronaut/;
s/br.com.zup.beagle.spring/br.com.zup.beagle.micronaut/g;

s/org.springframework.stereotype.Service/javax.inject.Singleton/;
s/org.springframework.web.bind.annotation.RestController/io.micronaut.http.annotation.Controller/;
s/org.springframework.web.bind.annotation.PostMapping/io.micronaut.http.annotation.Post/;
s/org.springframework.web.bind.annotation.GetMapping/io.micronaut.http.annotation.Get/;
s/org.springframework.web.bind.annotation.PutMapping/io.micronaut.http.annotation.Put/;
s/org.springframework.web.bind.annotation.DeleteMapping/io.micronaut.http.annotation.Delete/;

s/@Service/@Singleton/g;
s/@RestController/@Controller/g;
s/@GetMapping/@Get/g;
s/@PostMapping/@Post/g;
s/@PutMapping/@Put/g;
s/@DeleteMapping/@Delete/g;