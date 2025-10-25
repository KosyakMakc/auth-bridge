package io.github.kosyakmakc.authBridge.Commands;

import io.github.kosyakmakc.authBridge.AuthBridge;
import io.github.kosyakmakc.authBridge.Commands.Arguments.ArgumentFormatException;
import io.github.kosyakmakc.authBridge.Commands.Arguments.CommandArgument;
import io.github.kosyakmakc.authBridge.Commands.MinecraftCommands.MinecraftCommandBase;
import io.github.kosyakmakc.authBridge.MinecraftPlatform.MinecraftUser;
import io.github.kosyakmakc.authBridge.TestEnvironment.NullMinecraftPlatform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerArgumentsTest {
    @Test
    void simpleIntegerCheck() throws SQLException, IOException {
        class simpleIntegerCommand extends MinecraftCommandBase {
            private final int[] answers;
            public simpleIntegerCommand(int[] answers) {
                super("single argument", List.of(CommandArgument.ofInteger("single argument")));
                this.answers = answers;
            }

            @Override
            public void execute(MinecraftUser sender, List<Object> args) {
                for (var i = 0; i < answers.length; i++) {
                    Assertions.assertEquals(answers[i], (int) args.get(i));
                }
            }
        }

        record TestCase(int[] answers, String raw, boolean isError) { }
        var testCases = new TestCase[] {
                new TestCase(new int[] { 0 }, "0", false),
                new TestCase(new int[] { 123 }, "123", false),
                new TestCase(new int[] { -123 }, "-123", false),
                new TestCase(new int[] { 100_000 }, "1e5", false),
                new TestCase(new int[] { 255 }, "0xff", false),

                new TestCase(new int[] { 0 }, "0 0", false),
                new TestCase(new int[] { 123 }, "123 something", false),
                new TestCase(new int[] { -123 }, "-123 -123", false),
                new TestCase(new int[] { 100_000 }, "1e5  x", false),
                new TestCase(new int[] { 255 }, "0xff ", false),

                new TestCase(new int[] { 0 }, "", true),
                new TestCase(new int[] { 0 }, " ", true),
                new TestCase(new int[] { 0 }, "asd ", true),
                new TestCase(new int[] { 0  }, "e0d", true),
                new TestCase(new int[] { 0 }, "100asd", true),
                new TestCase(new int[] { 0 }, "asd100 ", true),
        };

        AuthBridge.Init(new NullMinecraftPlatform());

        for (var testCase : testCases) {
            try {
                var command = new simpleIntegerCommand(testCase.answers);
                command.init(AuthBridge.INSTANCE);
                command.handle(null, new StringReader(testCase.raw));

                if (testCase.isError) {
                    Assertions.fail("MUST failed | " + Arrays.stream(testCase.answers)
                            .mapToObj(Integer::toString)
                            .collect(Collectors.joining(",")) + " | " + testCase.raw + " | " + testCase.isError);
                }
            } catch (ArgumentFormatException e) {
                if (!testCase.isError) {
                    Assertions.fail("MUST passing | " + Arrays.stream(testCase.answers)
                                          .mapToObj(Integer::toString)
                                          .collect(Collectors.joining(",")) + " | " + testCase.raw + " | " + testCase.isError);
                }
            }
        }
    }
}
