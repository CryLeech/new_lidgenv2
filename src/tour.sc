theme: /

    state: TourRequest
        intent!: /Заявка на тур
        script:
            if ($parseTree._city && !$session.destination) {
                $session.destination = capitalize($parseTree._city.name);
            } else if ($parseTree._county && !$session.destination) {
                $session.destination = capitalize($parseTree._country.name);
            } else if (!$session.destination) {
                return $reactions.transition("/City");
            }
            if ($parseTree._date) {
                $session.date = $parseTree._date.value.slice(0,10);
            }

    state: City
        a: {{$answers.city}}

        state: HaveCity
            intent: /Город
            script:
                if ($parseTree._city) {
                    $session.destination = capitalize($parseTree._city.name);
                } else if ($parseTree._county) {
                    $session.destination = capitalize($parseTree._country.name);
                }
                $reactions.transition("/TourRequest");

        state: noMatch
            event: noMatch
            script:
                $temp.destination = $request.query;
                $reactions.answer("Правильно ли я понимаю, что вы хотите поехать в " + $request.query);
                $reactions.buttons(["Да"], ["Нет"]);

            state: Yes
                q: $yes
                script: 
                    $session.destination = $temp.destination;
                    $reactions.transition("/TourRequest");

            state: No
                q: $no
                script:
                    $session.cityCounter = $session.cityCounter || 0;
                    $session.cityCounter++;
                    if ($session.cityCounter < 3) {
                        $reactions.transition("/City");
                    } else {
                        $reactions.answer($answers.wrongCity);
                    }
