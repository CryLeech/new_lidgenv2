require: require.sc

theme: /

    state: Hello
        q!: *start *
        script:
            if (!$client.name) {
                $client.name = "";
            } else {
                $temp.name = ", " + $client.name;
            }
        a: {{$answers.hello}}
        script:
            $reactions.buttons(["Узнать погоду"], ["Записаться на тур"]);