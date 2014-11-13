package io.github.amethyst.common.test;

import com.insightfullogic.lambdabehave.JunitSuiteRunner;
import io.github.alyphen.amethyst.common.encrypt.EncryptionManager;
import org.junit.runner.RunWith;

import static com.insightfullogic.lambdabehave.Suite.describe;

@RunWith(JunitSuiteRunner.class)
public class EncryptionTests {{
    EncryptionManager encryptionManager = new EncryptionManager();
    String originalString = "Test string";
    describe("encryption", it -> {
        it.should("decrypt an encrypted message correctly", expect -> {
            expect.that(encryptionManager.decrypt(encryptionManager.encrypt(originalString))).is(originalString);
        });
    });
}}
