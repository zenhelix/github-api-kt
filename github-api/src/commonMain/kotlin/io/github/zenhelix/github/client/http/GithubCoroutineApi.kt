package io.github.zenhelix.github.client.http

import io.github.zenhelix.github.client.http.action.GithubActionsCoroutineApi
import io.github.zenhelix.github.client.http.activity.GithubActivityCoroutineApi
import io.github.zenhelix.github.client.http.apps.GithubAppsCoroutineApi
import io.github.zenhelix.github.client.http.billing.GithubBillingCoroutineApi
import io.github.zenhelix.github.client.http.branches.GithubBranchesCoroutineApi
import io.github.zenhelix.github.client.http.checks.GithubChecksCoroutineApi
import io.github.zenhelix.github.client.http.classroom.GithubClassroomCoroutineApi
import io.github.zenhelix.github.client.http.codescanning.GithubCodeScanningCoroutineApi
import io.github.zenhelix.github.client.http.codesecurity.GithubCodeSecurityCoroutineApi
import io.github.zenhelix.github.client.http.codesofconduct.GithubCodesOfConductCoroutineApi
import io.github.zenhelix.github.client.http.codespaces.GithubCodespacesCoroutineApi
import io.github.zenhelix.github.client.http.collaborators.GithubCollaboratorsCoroutineApi
import io.github.zenhelix.github.client.http.commits.GithubCommitsCoroutineApi
import io.github.zenhelix.github.client.http.copilot.GithubCopilotCoroutineApi
import io.github.zenhelix.github.client.http.dependabot.GithubDependabotCoroutineApi
import io.github.zenhelix.github.client.http.dependencygraph.GithubDependencyGraphCoroutineApi
import io.github.zenhelix.github.client.http.deploykeys.GithubDeployKeysCoroutineApi
import io.github.zenhelix.github.client.http.deployment.GithubDeploymentsCoroutineApi
import io.github.zenhelix.github.client.http.emoji.GithubEmojisCoroutineApi
import io.github.zenhelix.github.client.http.gist.GithubGistsCoroutineApi
import io.github.zenhelix.github.client.http.git.GithubGitCoroutineApi
import io.github.zenhelix.github.client.http.gitignore.GithubGitignoreCoroutineApi
import io.github.zenhelix.github.client.http.interactions.GithubInteractionsCoroutineApi
import io.github.zenhelix.github.client.http.issues.GithubIssuesCoroutineApi
import io.github.zenhelix.github.client.http.license.GithubLicensesCoroutineApi
import io.github.zenhelix.github.client.http.markdown.GithubMarkdownCoroutineApi
import io.github.zenhelix.github.client.http.meta.GithubMetaCoroutineApi
import io.github.zenhelix.github.client.http.metric.GithubMetricsCoroutineApi
import io.github.zenhelix.github.client.http.migration.GithubMigrationsCoroutineApi
import io.github.zenhelix.github.client.http.organization.GithubOrganizationsCoroutineApi
import io.github.zenhelix.github.client.http.packages.GithubPackagesCoroutineApi
import io.github.zenhelix.github.client.http.privateregistries.GithubPrivateRegistriesCoroutineApi
import io.github.zenhelix.github.client.http.project.GithubProjectCoroutineApi
import io.github.zenhelix.github.client.http.pullrequest.GithubPullRequestsCoroutineApi
import io.github.zenhelix.github.client.http.ratelimit.GithubRateLimitCoroutineApi
import io.github.zenhelix.github.client.http.reaction.GithubReactionsCoroutineApi
import io.github.zenhelix.github.client.http.release.GithubReleasesCoroutineApi
import io.github.zenhelix.github.client.http.repository.GithubRepositoriesCoroutineApi
import io.github.zenhelix.github.client.http.search.GithubSearchCoroutineApi
import io.github.zenhelix.github.client.http.secretscanning.GithubSecretScanningCoroutineApi
import io.github.zenhelix.github.client.http.securityadvisory.GithubSecurityAdvisoriesCoroutineApi
import io.github.zenhelix.github.client.http.team.GithubTeamsCoroutineApi
import io.github.zenhelix.github.client.http.user.GithubUsersCoroutineApi

public interface GithubCoroutineApi : GithubActionsCoroutineApi, GithubActivityCoroutineApi, GithubAppsCoroutineApi, GithubBillingCoroutineApi,
                                      GithubBranchesCoroutineApi, GithubChecksCoroutineApi, GithubClassroomCoroutineApi, GithubCodeScanningCoroutineApi,
                                      GithubCodeSecurityCoroutineApi, GithubCodesOfConductCoroutineApi, GithubCodespacesCoroutineApi,
                                      GithubCollaboratorsCoroutineApi, GithubCommitsCoroutineApi, GithubCopilotCoroutineApi, GithubDependabotCoroutineApi,
                                      GithubDependencyGraphCoroutineApi, GithubDeployKeysCoroutineApi, GithubDeploymentsCoroutineApi, GithubEmojisCoroutineApi,
                                      GithubGistsCoroutineApi, GithubGitCoroutineApi, GithubGitignoreCoroutineApi, GithubInteractionsCoroutineApi,
                                      GithubIssuesCoroutineApi, GithubLicensesCoroutineApi, GithubMarkdownCoroutineApi, GithubMetaCoroutineApi,
                                      GithubMetricsCoroutineApi, GithubMigrationsCoroutineApi, GithubOrganizationsCoroutineApi, GithubPackagesCoroutineApi,
                                      GithubPrivateRegistriesCoroutineApi, GithubProjectCoroutineApi, GithubPullRequestsCoroutineApi,
                                      GithubRateLimitCoroutineApi, GithubReactionsCoroutineApi, GithubReleasesCoroutineApi, GithubRepositoriesCoroutineApi,
                                      GithubSearchCoroutineApi, GithubSecretScanningCoroutineApi, GithubSecurityAdvisoriesCoroutineApi, GithubTeamsCoroutineApi,
                                      GithubUsersCoroutineApi
