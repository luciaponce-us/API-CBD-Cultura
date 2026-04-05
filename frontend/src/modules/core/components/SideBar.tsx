import { Flex } from "@chakra-ui/react";

type Props = React.ComponentProps<typeof Flex>;

export default function SideBar({ children, ...props }: Props) {
  return (
    <Flex
      bg="background"
      borderRadius="xl"
      boxShadow="md"
      p={6}
      direction="column"
      align="center"
      justify="flex-start"
      height="fit-content"
      gap={6}
      hideBelow="md"
      {...props}
    >
      {children}
    </Flex>
  );
}
