import { Link, Text } from "@chakra-ui/react";

type Props = {
  label: string;
  href: string;
  icon: React.ComponentType<{ size?: number }>;
};

export default function SocialLink({ label, href, icon: Icon }: Props) {
  return (
    <Link
      key={label}
      href={href}
      target="_blank"
      rel="noopener noreferrer"
      display="flex"
      alignItems="center"
      gap={3}
      color="principal.700"
      _hover={{ color: "principal.500" }}
    >
      <Icon size={30} />
      <Text fontSize="md">{label}</Text>
    </Link>
  );
}
