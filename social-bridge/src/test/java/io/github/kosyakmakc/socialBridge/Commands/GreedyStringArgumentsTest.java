package io.github.kosyakmakc.socialBridge.Commands;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.github.kosyakmakc.socialBridge.SocialBridge;
import io.github.kosyakmakc.socialBridge.Commands.Arguments.ArgumentFormatException;
import io.github.kosyakmakc.socialBridge.Commands.Arguments.CommandArgument;
import io.github.kosyakmakc.socialBridge.Commands.MinecraftCommands.MinecraftCommandBase;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.MinecraftUser;
import io.github.kosyakmakc.socialBridge.TestEnvironment.NullMinecraftPlatform;

public class GreedyStringArgumentsTest {
    @ParameterizedTest
    @CsvSource({
        "0, 0, false",
        "123, 123, false",
        "-123, -123, false",
        
        "0 0, 0 0, false",
        "123 something, 123 something, false",
        "-123 -123, -123 -123, false",

        "asd, asd, false",
        "e0d, e0d, false",
        "100asd, 100asd, false",
        "asd100, asd100, false",

        "1e2, 1e2, false",
        "0xff, 0xff, false",
    })
    void simpleIntegerCheck(String answer, String raw, boolean isError) throws SQLException, IOException {
        class simpleStringCommand extends MinecraftCommandBase {
            private final String answer;
            public simpleStringCommand(String answer) {
                super("single argument", List.of(CommandArgument.ofGreedyString("single argument")));
                this.answer = answer;
            }

            @Override
            public void execute(MinecraftUser sender, List<Object> args) {
                Assertions.assertEquals(answer, (String) args.get(0));
            }
        }

        NullMinecraftPlatform.Init();
        try {
            var command = new simpleStringCommand(answer);
            command.init(SocialBridge.INSTANCE);
            command.handle(null, new StringReader(raw));

            if (isError) {
                Assertions.fail("MUST failed | " + answer + " | " + raw + " | " + isError);
            }
        } catch (ArgumentFormatException e) {
            if (!isError) {
                Assertions.fail("MUST passing | " + answer + " | " + raw + " | " + isError);
            }
        }
    }
}
