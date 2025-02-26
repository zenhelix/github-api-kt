package io.github.zenhelix.github.client.http

import io.github.zenhelix.github.client.http.action.GithubActionsApi
import io.github.zenhelix.github.client.http.activity.GithubActivityApi
import io.github.zenhelix.github.client.http.apps.GithubAppsApi
import io.github.zenhelix.github.client.http.billing.GithubBillingApi
import io.github.zenhelix.github.client.http.branches.GithubBranchesApi
import io.github.zenhelix.github.client.http.checks.GithubChecksApi
import io.github.zenhelix.github.client.http.classroom.GithubClassroomApi
import io.github.zenhelix.github.client.http.codescanning.GithubCodeScanningApi
import io.github.zenhelix.github.client.http.codesecurity.GithubCodeSecurityApi
import io.github.zenhelix.github.client.http.codesofconduct.GithubCodesOfConductApi
import io.github.zenhelix.github.client.http.codespaces.GithubCodespacesApi
import io.github.zenhelix.github.client.http.collaborators.GithubCollaboratorsApi
import io.github.zenhelix.github.client.http.commits.GithubCommitsApi
import io.github.zenhelix.github.client.http.copilot.GithubCopilotApi
import io.github.zenhelix.github.client.http.dependabot.GithubDependabotApi
import io.github.zenhelix.github.client.http.dependencygraph.GithubDependencyGraphApi
import io.github.zenhelix.github.client.http.deploykeys.GithubDeployKeysApi
import io.github.zenhelix.github.client.http.deployment.GithubDeploymentsApi
import io.github.zenhelix.github.client.http.emoji.GithubEmojisApi
import io.github.zenhelix.github.client.http.gist.GithubGistsApi
import io.github.zenhelix.github.client.http.git.GithubGitApi
import io.github.zenhelix.github.client.http.gitignore.GithubGitignoreApi
import io.github.zenhelix.github.client.http.interactions.GithubInteractionsApi
import io.github.zenhelix.github.client.http.issues.GithubIssuesApi
import io.github.zenhelix.github.client.http.license.GithubLicensesApi
import io.github.zenhelix.github.client.http.markdown.GithubMarkdownApi
import io.github.zenhelix.github.client.http.meta.GithubMetaApi
import io.github.zenhelix.github.client.http.metric.GithubMetricsApi
import io.github.zenhelix.github.client.http.migration.GithubMigrationsApi
import io.github.zenhelix.github.client.http.organization.GithubOrganizationsApi
import io.github.zenhelix.github.client.http.packages.GithubPackagesApi
import io.github.zenhelix.github.client.http.pages.GithubPagesApi
import io.github.zenhelix.github.client.http.privateregistries.GithubPrivateRegistriesApi
import io.github.zenhelix.github.client.http.project.GithubProjectApi
import io.github.zenhelix.github.client.http.pullrequest.GithubPullRequestsApi
import io.github.zenhelix.github.client.http.ratelimit.GithubRateLimitApi
import io.github.zenhelix.github.client.http.reaction.GithubReactionsApi
import io.github.zenhelix.github.client.http.release.GithubReleasesApi
import io.github.zenhelix.github.client.http.repository.GithubRepositoriesApi
import io.github.zenhelix.github.client.http.search.GithubSearchApi
import io.github.zenhelix.github.client.http.secretscanning.GithubSecretScanningApi
import io.github.zenhelix.github.client.http.securityadvisory.GithubSecurityAdvisoriesApi
import io.github.zenhelix.github.client.http.team.GithubTeamsApi
import io.github.zenhelix.github.client.http.user.GithubUsersApi

public interface GithubApi : GithubActionsApi, GithubActivityApi, GithubAppsApi, GithubBillingApi, GithubBranchesApi, GithubChecksApi, GithubClassroomApi,
                             GithubCodeScanningApi, GithubCodeSecurityApi, GithubCodesOfConductApi,
                             GithubCodespacesApi, GithubCollaboratorsApi, GithubCommitsApi, GithubCopilotApi, GithubDependabotApi,
                             GithubDependencyGraphApi, GithubDeployKeysApi, GithubDeploymentsApi, GithubEmojisApi, GithubGistsApi,
                             GithubGitApi, GithubGitignoreApi, GithubInteractionsApi, GithubIssuesApi, GithubLicensesApi, GithubMarkdownApi,
                             GithubMetaApi, GithubMetricsApi, GithubMigrationsApi, GithubOrganizationsApi, GithubPackagesApi,
                             GithubPagesApi, GithubPrivateRegistriesApi, GithubProjectApi, GithubPullRequestsApi, GithubRateLimitApi, GithubReactionsApi,
                             GithubReleasesApi, GithubRepositoriesApi, GithubSearchApi, GithubSecretScanningApi, GithubSecurityAdvisoriesApi, GithubTeamsApi,
                             GithubUsersApi


