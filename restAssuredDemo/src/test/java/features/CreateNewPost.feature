@NewPost
Feature:
  Create a new Post

  @CreateSuccessfully
  Scenario Outline: Create a new post informing the title and author
    Given Provide the values "<title>""<author>"
    When Send a Post Request
    Then Status_code is equal to <status_code>

    Examples:
    |title             |author              |status_code|
    |new post test     |maikon gonçalves    |201        |
