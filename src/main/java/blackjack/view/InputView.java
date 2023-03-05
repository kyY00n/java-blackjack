package blackjack.view;

import java.util.List;
import java.util.Scanner;

import blackjack.domain.participant.Name;

public class InputView {
    private final Scanner scanner = new Scanner(System.in);

    public List<String> readPlayerNames() {
        System.out.println("게임에 참여할 사람의 이름을 입력하세요.(쉼표 기준으로 분리)");
        return List.of(scanner.nextLine().split(","));
    }

    public String readCommandToAddCardOrNot(Name name) {
        System.out.printf("%s는 한장의 카드를 더 받겠습니까?(예는 y, 아니오는 n) %n", name.getValue());
        return scanner.nextLine();
    }
}